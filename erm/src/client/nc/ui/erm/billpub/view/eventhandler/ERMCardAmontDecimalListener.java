package nc.ui.erm.billpub.view.eventhandler;

import nc.itf.fi.pub.Currency;
import nc.ui.erm.matterapp.common.MatterAppUiUtil;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelDecimalListener3;
import nc.vo.bd.currtype.CurrtypeVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;

public class ERMCardAmontDecimalListener
  implements IBillModelDecimalListener3
{
  public static final String RATE_TYPE_YB = "yb";
  public static final String RATE_TYPE_LOCAL = "local";
  public static final String RATE_TYPE_GROUP = "group";
  public static final String RATE_TYPE_GLOBAL = "global";
  private String[] targetKeys;
  private String rateType;
  private BillCardPanel cardPanel;
  private BillModel billmodel;

  public ERMCardAmontDecimalListener(BillModel billmodel, BillCardPanel cardPanel, String[] targetKeys, String rateType)
  {
    this.targetKeys = targetKeys;
    this.rateType = rateType;
    this.cardPanel = cardPanel;
    this.billmodel = billmodel;
    this.billmodel.addDecimalListener(this);
  }

  public String getSource()
  {
    return "assume_org";
  }

  public int getDecimalFromSource(int row, Object okValue)
  {
    int digit = 8;
    if (this.billmodel.isImporting())
      return digit;
    try
    {
      if ("yb".equals(this.rateType))
        return Currency.getCurrDigit(getHeadItemStrValue("bzbm"));
      if ("local".equals(this.rateType))
        return Currency.getCurrDigit(Currency.getOrgLocalCurrPK(okValue.toString()));
      if ("group".equals(this.rateType))
        return Currency.getCurrDigit(Currency.getGroupCurrpk(MatterAppUiUtil.getPK_group()));
      if ("global".equals(this.rateType))
        return Currency.getCurrDigit(Currency.getGlobalCurrPk(null));
    }
    catch (BusinessException e) {
      ExceptionHandler.consume(e);
    }

    return digit;
  }

  private String getHeadItemStrValue(String itemKey)
  {
    BillItem headItem = this.cardPanel.getHeadItem(itemKey);
    return headItem == null ? null : (String)headItem.getValueObject();
  }

  public String[] getTarget() {
    return this.targetKeys;
  }

  public boolean isTarget(BillItem item) {
    for (int i = 0; i < this.targetKeys.length; i++) {
      if (this.targetKeys[i].equals(item.getKey())) {
        return true;
      }
    }
    return false;
  }

  public int getRoundingModeFromSource(int row, Object okValue)
  {
    int roundType = 4;
    try {
      String pk_currtype = null;

      if ("yb".equals(this.rateType))
        pk_currtype = getHeadItemStrValue("bzbm");
      else if ("local".equals(this.rateType))
        pk_currtype = Currency.getOrgLocalCurrPK(okValue.toString());
      else if ("group".equals(this.rateType))
        pk_currtype = Currency.getGroupCurrpk(MatterAppUiUtil.getPK_group());
      else if ("global".equals(this.rateType)) {
        pk_currtype = Currency.getGlobalCurrPk(null);
      }

      CurrtypeVO cirrtype = Currency.getCurrInfo(pk_currtype);
      if (cirrtype != null)
        return cirrtype.getRoundtype().intValue();
    }
    catch (BusinessException e) {
      ExceptionHandler.consume(e);
    }
    return roundType;
  }
}