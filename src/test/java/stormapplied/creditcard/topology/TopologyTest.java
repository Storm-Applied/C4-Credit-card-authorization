package stormapplied.creditcard.topology;

import backtype.storm.Config;
import backtype.storm.ILocalCluster;
import backtype.storm.Testing;
import backtype.storm.generated.StormTopology;
import backtype.storm.testing.CompleteTopologyParam;
import backtype.storm.testing.MkClusterParam;
import backtype.storm.testing.MockedSources;
import backtype.storm.testing.TestJob;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Values;
import org.junit.Test;
import stormapplied.creditcard.Order;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TopologyTest {
  @Test
  public void verifyProperValuesAreEmittedByEachBolt() {
    Config config = new Config();
    config.setDebug(true);

    MkClusterParam clusterParam = new MkClusterParam();
    clusterParam.setSupervisors(1);
    clusterParam.setDaemonConf(config);

    Testing.withSimulatedTimeLocalCluster(clusterParam, new TestJob() {
      @Override
      public void run(ILocalCluster cluster) {
        MockedSources mockedSources = new MockedSources();
        mockedSources.addMockData("file-based-spout", new Values(new Order(1234, 5678, 1111222233334444L, "012014", 123, 42.23)));

        Config config = new Config();
        config.setDebug(true);

        CompleteTopologyParam topologyParam = new CompleteTopologyParam();
        topologyParam.setMockedSources(mockedSources);
        topologyParam.setStormConf(config);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("file-based-spout", new Spout());

        builder.setBolt("verify-order-status", new VerifyOrderStatus())
            .shuffleGrouping("file-based-spout");

        builder.setBolt("authorize-order", new AuthorizeCreditCard())
            .shuffleGrouping("verify-order-status");

        builder.setBolt("accepted-notification", new ProcessedOrderNotification())
            .shuffleGrouping("authorize-order");

        StormTopology topology = builder.createTopology();

        Map result = Testing.completeTopology(cluster, topology, topologyParam);
        assertEquals(3, result.size());
        // TODO: provide better valdiation after refactoring some of this topology's code
      }
    });
  }
}