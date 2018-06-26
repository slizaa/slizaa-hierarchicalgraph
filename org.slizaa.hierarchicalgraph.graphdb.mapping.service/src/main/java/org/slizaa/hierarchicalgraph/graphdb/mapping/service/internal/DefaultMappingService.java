package org.slizaa.hierarchicalgraph.graphdb.mapping.service.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.GraphFactoryFunctions.createDependencies;
import static org.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.GraphFactoryFunctions.createFirstLevelElements;
import static org.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.GraphFactoryFunctions.createHierarchy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slizaa.core.boltclient.IBoltClient;
import org.slizaa.hierarchicalgraph.core.model.HGRootNode;
import org.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.core.model.INodeSource;
import org.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGRootNodeImpl;
import org.slizaa.hierarchicalgraph.core.model.spi.IAutoExpandInterceptor;
import org.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import org.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import org.slizaa.hierarchicalgraph.graphdb.mapping.cypher.IBoltClientAware;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingParticipator;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import org.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingException;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IHierarchyDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import org.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import org.slizaa.hierarchicalgraph.graphdb.model.GraphDbHierarchicalgraphFactory;
import org.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import org.slizaa.hierarchicalgraph.graphdb.model.GraphDbRootNodeSource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
@Component
public class DefaultMappingService implements IMappingService {

  /** - */
  private List<IMappingParticipator> _mappingParticipators    = new CopyOnWriteArrayList<>();

  /** create the node source creator function */
  static Function<Long, INodeSource> createNodeSourceFunction = (id) -> {

                                                                // create the node source
                                                                INodeSource nodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE
                                                                    .createGraphDbNodeSource();
                                                                nodeSource.setIdentifier(id);

                                                                // return the result
                                                                return nodeSource;
                                                              };

  @Reference()
  public void addMappingParticipator(IMappingParticipator mappingParticipator) {
    this._mappingParticipators.add(mappingParticipator);
  }

  public void removeMappingParticipator(IMappingParticipator mappingParticipator) {
    this._mappingParticipators.remove(mappingParticipator);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public HGRootNode convert(IMappingProvider mappingDescriptor, final IBoltClient boltClient,
      IProgressMonitor progressMonitor) throws MappingException {

    checkNotNull(mappingDescriptor);
    checkNotNull(boltClient);

    try {
      // create the sub monitor
      SubMonitor subMonitor = progressMonitor != null ? SubMonitor.convert(progressMonitor, 100) : null;

      // create the root element
      final HGRootNode rootNode = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
      GraphDbRootNodeSource rootNodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbRootNodeSource();
      rootNodeSource.setIdentifier(-1l);
      rootNodeSource.setBoldClient(boltClient);
      rootNode.setNodeSource(rootNodeSource);

      // process root, hierarchy and dependency queries
      IHierarchyDefinitionProvider hierarchyProvider = initializeBoltClientAwareMappingProviderComponent(
          mappingDescriptor.getHierarchyDefinitionProvider(), boltClient, progressMonitor);

      if (hierarchyProvider != null) {

        //
        report(subMonitor, "Requesting root nodes...");
        List<Long> rootNodes = hierarchyProvider.getToplevelNodeIds();

        report(subMonitor, "Creating root nodes...");
        createFirstLevelElements(rootNodes.toArray(new Long[0]), rootNode, createNodeSourceFunction, progressMonitor);

        //
        report(subMonitor, "Requesting nodes...");
        List<Long[]> parentChildNodeIds = hierarchyProvider.getParentChildNodeIds();

        report(subMonitor, "Creating nodes...");
        createHierarchy(parentChildNodeIds, rootNode, createNodeSourceFunction, progressMonitor);

        // filter 'dangling' nodes
        removeDanglingNodes(rootNode);

        //
        IDependencyDefinitionProvider dependencyProvider = initializeBoltClientAwareMappingProviderComponent(
            mappingDescriptor.getDependencyDefinitionProvider(), boltClient, progressMonitor);

        if (dependencyProvider != null) {

          report(subMonitor, "Creating dependencies...");

          //
          createDependencies(dependencyProvider.getDependencies(), rootNode,
              (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, progressMonitor);
        }
      }

      // register default extensions
      rootNode.registerExtension(IBoltClient.class, boltClient);
      rootNode.registerExtension(IProxyDependencyResolver.class, new CustomProxyDependencyResolver());
      rootNode.registerExtension(IMappingProvider.class, mappingDescriptor);
      rootNode.registerExtension(INodeComparator.class, mappingDescriptor.getNodeComparator());
      rootNode.registerExtension(ILabelDefinitionProvider.class, mappingDescriptor.getLabelDefinitionProvider());

      //
      rootNode.registerExtension(IAutoExpandInterceptor.class, node -> {

        Optional<GraphDbNodeSource> nodeSource = node.getNodeSource(GraphDbNodeSource.class);
        if (nodeSource.isPresent()) {
          // TODO
          return nodeSource.get().getLabels().contains("Resource");
        }
        return false;
      });

      //
      for (IMappingParticipator mappingParticipator : this._mappingParticipators) {
        mappingParticipator.postCreate(rootNode, mappingDescriptor, boltClient);
      }

      //
      // return addEditingDomain(rootNode);
      return rootNode;
    }
    //
    catch (Exception e) {
      throw new MappingException(e.getMessage(), e);
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param service
   * @param boltClient
   * @param progressMonitor
   * @throws Exception
   */
  private <T> T initializeBoltClientAwareMappingProviderComponent(T component, final IBoltClient boltClient,
      IProgressMonitor progressMonitor) throws Exception {

    if (component instanceof IBoltClientAware) {
      ((IBoltClientAware) component).initialize(boltClient, progressMonitor);
    }

    return component;
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   */
  private void removeDanglingNodes(final HGRootNode rootNode) {

    //
    List<Object> nodeKeys2Remove = ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().entrySet().stream()
        .filter((n) -> {
          try {
            return !new Long(0).equals(n.getValue().getIdentifier()) && n.getValue().getRootNode() == null;
          } catch (Exception e) {
            return true;
          }
        }).map(n -> n.getKey()).collect(Collectors.toList());

    //
    nodeKeys2Remove.forEach(k -> ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().remove(k));
  }

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @param dependencyQueries
   * @param dependencyLoopMonitor
   */
  // TODO
  // private void resolveDependencyQueries(final HGRootNode rootNode, List<DependencyQuery> dependencyQueries,
  // SubMonitor dependencyLoopMonitor) {
  //
  // //
  // dependencyQueries.forEach((dependencyQuery) -> {
  //
  // try {
  // SubMonitor iterationMonitor = dependencyLoopMonitor != null ? dependencyLoopMonitor.split(1) : null;
  //
  // // request dependencies
  // report(iterationMonitor, "Requesting dependencies...");
  // JsonArray jsonArray = dependencyQuery.getFuture().get().getAsJsonArray("data");
  //
  // // create dependencies
  // report(iterationMonitor, "Creating dependencies...");
  // createDependencies(jsonArray, rootNode,
  // (id, type) -> GraphFactoryFunctions.createDependencySource(id, type,
  // dependencyQueries.size() > 1 ? dependencyQuery.getDependencyMapping() : null),
  // dependencyQuery.getDependencyMapping().isProxyDependency(), false, iterationMonitor);
  //
  // } catch (Exception e) {
  // throw new HierarchicalGraphMappingException(e);
  // }
  // });
  // }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param rootNode
  // * @return
  // */
  // private HGRootNode addEditingDomain(HGRootNode rootNode) {
  //
  // //
  // Resource resource = new ResourceSetImpl().createResource(URI.createURI("memory://localhost/hierarchicalgraph"));
  //
  // //
  // BasicCommandStack basicCommandStack = new BasicCommandStack();
  // AdapterFactoryEditingDomain adapterFactoryEditingDomain = new AdapterFactoryEditingDomain(
  // new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE), basicCommandStack);
  // resource.eAdapters().add(new AdapterFactoryEditingDomain.EditingDomainProvider(adapterFactoryEditingDomain));
  //
  // //
  // resource.getContents().add(rootNode);
  //
  // //
  // return rootNode;
  // }

  /**
   * <p>
   * </p>
   *
   * @param iterationMonitor
   * @param taskName
   */
  private void report(SubMonitor iterationMonitor, String taskName) {
    if (iterationMonitor != null) {
      iterationMonitor.setTaskName(taskName);
    }
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
  // */
  // private class AggregatedDependencyQueryHolder {
  //
  // /** - */
  // private Future<StatementResult> _future;
  //
  // /** - */
  // private AggregatedDependencyQuery _aggregatedDependencyQuery;
  //
  // /**
  // * <p>
  // * Creates a new instance of type {@link AggregatedDependencyQuery}.
  // * </p>
  // *
  // * @param aggregatedDependencyQuery
  // * @param future
  // */
  // public AggregatedDependencyQueryHolder(AggregatedDependencyQuery aggregatedDependencyQuery,
  // Future<StatementResult> future) {
  // this._future = checkNotNull(future);
  // this._aggregatedDependencyQuery = checkNotNull(aggregatedDependencyQuery);
  // }
  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @return
  // */
  // public Future<StatementResult> getFuture() {
  // return _future;
  // }
  //
  // /**
  // * <p>
  // * </p>
  // *
  // * @return
  // */
  // public AggregatedDependencyQuery getAggregatedDependencyQuery() {
  // return _aggregatedDependencyQuery;
  // }
  // }
}
