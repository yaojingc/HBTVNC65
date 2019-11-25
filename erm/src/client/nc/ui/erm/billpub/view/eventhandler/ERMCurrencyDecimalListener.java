package nc.ui.erm.billpub.view.eventhandler;

import nc.itf.fi.pub.Currency;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillListPanel;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.IBillModelDecimalListener2;
import nc.vo.fipub.exception.ExceptionHandler;

public class ERMCurrencyDecimalListener
  implements IBillModelDecimalListener2
{
  private BillListPanel listPanel;

  public ERMCurrencyDecimalListener(BillListPanel listPanel)
  {
    this.listPanel = listPanel;
  }

  public String[] getTarget() {
    return new String[] { "bbhl" };
  }

  public int getDecimalFromSource(int row, Object value) {
    if ((value == null) || (value.toString().length() == 0))
      return 2;
    try
    {
      String pk_org = (String)this.listPanel.getHeadBillModel().getValueAt(row, "pk_org");
      String pk_currency = (String)this.listPanel.getHeadBillModel().getValueAt(row, "bzbm_ID");
      return Currency.getRateDigit(pk_org, pk_currency, Currency.getOrgLocalCurrPK(pk_org));
    } catch (Exception e) {
      ExceptionHandler.consume(e);
    }return 2;
  }

  public String getSource()
  {
    return "pk_jkbx";
  }

  public boolean isTarget(BillItem item) {
    boolean result = false;
    if ("bbhl".equals(item.getKey())) {
      result = true;
    }
    return result;
  }
}