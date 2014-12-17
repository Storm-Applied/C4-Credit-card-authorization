package stormapplied.creditcard.topology;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import stormapplied.creditcard.Order;

public class Spout extends BaseRichSpout {
  private SpoutOutputCollector outputCollector;
  private List<Order> orders;

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("order"));
  }

  @Override
  public void open(Map map,
                   TopologyContext topologyContext,
                   SpoutOutputCollector outputCollector) {
    this.outputCollector = outputCollector;

    try {
      List<String> jsonList = IOUtils.readLines(ClassLoader.getSystemResourceAsStream("orders.txt"),
                                                Charset.defaultCharset().name());
      orders = convertJsonList(jsonList);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Order> convertJsonList(List<String> jsonList) {
    List<Order> orders = new ArrayList<Order>(jsonList.size());
    Gson gson = new Gson();
    for (String json : jsonList) {
      Order order = gson.fromJson(json, Order.class);
      orders.add(order);
    }
    return orders;
  }

  @Override
  public void nextTuple() {
    for (Order order : orders) {
      outputCollector.emit(new Values(order));
    }
  }

  @Override
  public void ack(Object msgId) {
    super.ack(msgId);
  }

  @Override
  public void fail(Object msgId) {
    super.fail(msgId);
  }
}