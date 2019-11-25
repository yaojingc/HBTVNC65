package nc.ui.erm.billpub.view.eventhandler;

import java.util.List;
import javax.swing.JComponent;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.model.FreeCustRefModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.util.ErUiUtil;
import nc.ui.erm.view.ERMBillForm;
import nc.ui.erm.view.ErmCardPanelEventTransformer;
import nc.ui.org.ref.DeptDefaultRefModel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardBeforeEditListener;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillItemEvent;
import nc.ui.uif2.editor.BillForm;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.exception.ExceptionHandler;
import nc.vo.pub.BusinessException;

public class InitBillCardBeforeEditListener
  implements BillCardBeforeEditListener
{
  private BillForm editor;

  public InitBillCardBeforeEditListener(BillForm editor)
  {
    this.editor = editor;
  }

  public boolean beforeEdit(BillItemEvent e)
  {
    String key = e.getItem().getKey();
    BillItem billItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem(key);
    try
    {
      if (JKBXHeaderVO.DEPTID_V.equals(key)) {
        BillItem headItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem("dwbm");
        if (headItem != null) {
          String dwbm = (String)headItem.getValueObject();
          getHeadFieldHandle().beforeEditDept_v(dwbm, JKBXHeaderVO.DEPTID_V);
        }
      } else if (JKBXHeaderVO.FYDEPTID_V.equals(key)) {
        BillItem headItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem("fydwbm");
        if (headItem != null) {
          String fydwbm = (String)headItem.getValueObject();
          getHeadFieldHandle().beforeEditDept_v(fydwbm, JKBXHeaderVO.FYDEPTID_V);
        }
      } else if ("deptid".equals(key)) {
        BillItem headItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem("dwbm");
        if (headItem != null) {
          String dwbm = (String)headItem.getValueObject();
          beforeEditDept(dwbm, "deptid");
        }
      } else if ("fydeptid".equals(key)) {
        BillItem headItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem("fydwbm");
        if (headItem != null) {
          String dwbm = (String)headItem.getValueObject();
          beforeEditDept(dwbm, "fydeptid");
        }
      } else if ("jkbxr".equals(key)) {
        getHeadFieldHandle().initJkbxr();
        ErUiUtil.filterLeavePowerShowAndUI(billItem, false);
      } else if ((key != null) && (key.startsWith("zyx"))) {
        filterZyxField(key);
      } else if ("custaccount".equals(key)) {
        beforeEditCustaccount();
      } else if ("freecust".equals(key)) {
        beforeEditFreecust();
      } else if ("skyhzh".equals(key)) {
        beforeEditSkyhzh();
      } else if ("jobid".equals(key)) {
        getHeadFieldHandle().initProj();
      } else if (JKBXHeaderVO.PK_CASHACCOUNT.equals(key)) {
        getHeadFieldHandle().initCashAccount();
      } else if ("pk_checkele".equals(key)) {
        getHeadFieldHandle().initPk_Checkele();
      } else if ("cashproj".equals(key)) {
        getHeadFieldHandle().initCashProj();
      } else if (JKBXHeaderVO.PK_RESACOSTCENTER.equals(key)) {
        getHeadFieldHandle().initResaCostCenter();
      } else if ("projecttask".equals(key)) {
        getHeadFieldHandle().initProjTask();
      } else if ("fkyhzh".equals(key)) {
        getHeadFieldHandle().initFkyhzh();
      } else if ("szxmid".equals(key)) {
        getHeadFieldHandle().initSzxm();
      } else if ("receiver".equals(key)) {
        ErUiUtil.filterLeavePowerShowAndUI(billItem, false);
      }

      if ((!"pk_org_v".equals(key)) && (!"pk_org".equals(key)) && 
        (billItem != null) && ((billItem.getComponent() instanceof UIRefPane)) && (((UIRefPane)billItem.getComponent()).getRefModel() != null))
        CrossCheckUtil.checkRule("Y", key, this.editor);
    }
    catch (BusinessException e1)
    {
      ExceptionHandler.handleExceptionRuntime(e1);
      return false;
    }
    return ((ERMBillForm)this.editor).getErmEventTransformer().beforeEdit(e);
  }

  private void beforeEditSkyhzh() {
    getHeadFieldHandle().initSkyhzh();
  }

  private void beforeEditFreecust()
  {
    UIRefPane refPane = getHeadItemUIRefPane("freecust");

    ((FreeCustRefModel)refPane.getRefModel()).setCustomSupplier(getFreeCustFilterCond());
  }

  private void beforeEditCustaccount()
  {
    getHeadFieldHandle().initCustAccount();
  }

  private String getFreeCustFilterCond()
  {
    Integer paytarget = (Integer)getHeadValue("paytarget");
    String pk_custsup = null;
    if ((paytarget != null) && (paytarget.intValue() == 1)) {
      pk_custsup = getHeadItemStrValue("hbbm");
    } else if ((paytarget != null) && (paytarget.intValue() == 2)) {
      pk_custsup = getHeadItemStrValue("customer");
    } else {
      String hbbm = getHeadItemStrValue("hbbm");
      if (hbbm != null)
        pk_custsup = hbbm;
      else {
        pk_custsup = getHeadItemStrValue("customer");
      }
    }
    return pk_custsup;
  }

  private String getHeadItemStrValue(String itemKey) {
    BillItem headItem = this.editor.getBillCardPanel().getHeadItem(itemKey);
    return headItem == null ? null : (String)headItem.getValueObject();
  }

  private UIRefPane getHeadItemUIRefPane(String key) {
    JComponent component = this.editor.getBillCardPanel().getHeadItem(key).getComponent();
    return (component instanceof UIRefPane) ? (UIRefPane)component : null;
  }

  private Object getHeadValue(String key) {
    BillItem headItem = this.editor.getBillCardPanel().getHeadItem(key);
    if (headItem == null) {
      headItem = this.editor.getBillCardPanel().getTailItem(key);
    }
    if (headItem == null) {
      return null;
    }
    return headItem.getValueObject();
  }

  private void filterZyxField(String key)
  {
    BillItem headItem = ((ErmBillBillForm)this.editor).getBillCardPanel().getHeadItem(key);
    if (((headItem.getComponent() instanceof UIRefPane)) && (((UIRefPane)headItem.getComponent()).getRefModel() != null)) {
      ErmBillBillForm ermBillFom = (ErmBillBillForm)this.editor;
      String pk_org = null;
      if ((ermBillFom.getOrgRefFields("pk_org") != null) && (ermBillFom.getOrgRefFields("pk_org").contains(key))) {
        BillItem item = ermBillFom.getBillCardPanel().getHeadItem("pk_org");
        if (item != null)
          pk_org = (String)item.getValueObject();
      }
      else if ((ermBillFom.getOrgRefFields("dwbm") != null) && (ermBillFom.getOrgRefFields("dwbm").contains(key))) {
        BillItem item = ermBillFom.getBillCardPanel().getHeadItem("dwbm");
        if (item != null)
          pk_org = (String)item.getValueObject();
      }
      else if ((ermBillFom.getOrgRefFields("fydwbm") != null) && (ermBillFom.getOrgRefFields("fydwbm").contains(key))) {
        BillItem item = ermBillFom.getBillCardPanel().getHeadItem("fydwbm");
        if (item != null)
          pk_org = (String)item.getValueObject();
      }
      else if ((ermBillFom.getOrgRefFields(JKBXHeaderVO.PK_PAYORG) != null) && (ermBillFom.getOrgRefFields(JKBXHeaderVO.PK_PAYORG).contains(key))) {
        BillItem item = ermBillFom.getBillCardPanel().getHeadItem(JKBXHeaderVO.PK_PAYORG);
        if (item != null)
          pk_org = (String)item.getValueObject();
      }
      else {
        BillItem item = ermBillFom.getBillCardPanel().getHeadItem("pk_org");
        if (item != null) {
          pk_org = (String)item.getValueObject();
        }
      }

      ((UIRefPane)headItem.getComponent()).getRefModel().setPk_org(pk_org);
    }
  }

  private void beforeEditDept(String dwbm, String deptid) {
    BillItem headItem = this.editor.getBillCardPanel().getHeadItem(deptid);
    if (headItem != null) {
      UIRefPane refPane = (UIRefPane)headItem.getComponent();
      DeptDefaultRefModel model = (DeptDefaultRefModel)refPane.getRefModel();
      model.setPk_org(dwbm);
    }
  }

  private HeadFieldHandleUtil getHeadFieldHandle() {
    return ((ErmBillBillForm)this.editor).getEventHandle().getHeadFieldHandle();
  }
}