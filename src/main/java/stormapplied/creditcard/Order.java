package stormapplied.creditcard;

import java.io.Serializable;

public class Order implements Serializable {
  private long id;
  private long customerId;
  private long creditCardNumber;
  private String creditCardExpiration;
  private int creditCardCode;
  private double chargeAmount;

  public Order(long id,
               long customerId,
               long creditCardNumber,
               String creditCardExpiration,
               int creditCardCode,
               double chargeAmount) {
    this.id = id;
    this.customerId = customerId;
    this.creditCardNumber = creditCardNumber;
    this.creditCardExpiration = creditCardExpiration;
    this.creditCardCode = creditCardCode;
    this.chargeAmount = chargeAmount;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", customerId=" + customerId +
        ", creditCardNumber=" + creditCardNumber +
        ", creditCardExpiration='" + creditCardExpiration + '\'' +
        ", creditCardCode=" + creditCardCode +
        ", chargeAmount=" + chargeAmount +
        '}';
  }
}