package nc.ui.erm.billpub.view.eventhandler;

import nc.itf.fi.pub.Currency;
import nc.ui.er.util.BXUiUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelDecimalListener2;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;

public class ERMCardCShareRateListener
  implements IBillModelDecimalListener2
{
  public static final String RATE_TYPE_LOCAL = "local";
  public static final String RATE_TYPE_GROUP = "group";
  public static final String RATE_TYPE_GLOBAL = "global";
  private String orgField;
  private String[] target;
  private String rateType;
  private BillModel model;
  private BillCardPanel billCardPanel;

  public ERMCardCShareRateListener(BillCardPanel billCardPanel, BillModel model, String orgField, String[] target, String rateType)
  {
    this.model = model;
    this.orgField = orgField;
    this.target = target;
    this.rateType = rateType;
    this.billCardPanel = billCardPanel;

    this.model.addDecimalListener(this);
  }

  public String[] getTarget()
  {
    return this.target;
  }

  public int getDecimalFromSource(int row, Object value) {
    int hlPrecision = 8;

    if (this.model.isImporting()) {
      return hlPrecision;
    }
    String pk_org = (String)value;

    String pk_currtype = this.billCardPanel.getHeadItem("bzbm").getValueObject().toString();
    try
    {
      if ("local".equals(this.rateType))
        hlPrecision = Currency.getRateDigit(pk_org, pk_currtype, Currency.getOrgLocalCurrPK(pk_org));
      else if ("group".equals(this.rateType))
        hlPrecision = Currency.getGroupRateDigit(pk_org, BXUiUtil.getPK_group(), pk_currtype);
      else if ("global".equals(this.rateType))
      {
        hlPrecision = Currency.getGlobalRateDigit(pk_org, pk_currtype);
      }
    } catch (BusinessException e) {
      ExceptionHandler.consume(e);
    }

    return hlPrecision;
  }

  public String getSource()
  {
    return this.orgField;
  }

  public boolean isTarget(BillItem item) {
    boolean result = false;
    for (int i = 0; i < this.target.length; i++) {
      if (this.target[i].equals(item.getKey())) {
        result = true;
        break;
      }
    }
    return result;
  }
}