package stormapplied.creditcard.topology;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import stormapplied.creditcard.Order;
import stormapplied.creditcard.services.NotificationService;

import java.util.Map;

public class ProcessedOrderNotification extends BaseBasicBolt {
  private NotificationService notificationService;

  @Override
  public void prepare(Map config,
                      TopologyContext context) {
    notificationService = new NotificationService();
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    // nothing to declare
  }

  @Override
  public void execute(Tuple tuple,
                      BasicOutputCollector outputCollector) {
    Order order = (Order) tuple.getValueByField("order");
    notificationService.notifyOrderHasBeenProcessed(order);
  }
}