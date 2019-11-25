package nc.ui.erm.billpub.view.eventhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.erm.view.ERMOrgPane;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillTabbedPane;
import nc.ui.uif2.editor.BillForm;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.erm.matterapp.MatterAppConvResVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

public class AddFromMtAppEditorUtil
{
  private ErmBillBillForm billform;
  private static final String HEAD = "head";
  private static final String BUS_BODY = "bus_body";

  public AddFromMtAppEditorUtil(ErmBillBillForm billform)
  {
    this.billform = billform;
  }

  public void resetBillItemOnAdd()
    throws BusinessException
  {
    JKBXVO jkbxvo = (JKBXVO)((ErmBillBillForm)getEditor()).getResVO().getBusiobj();

    List ctrlFieldList = (List)((ErmBillBillForm)getEditor()).getResVO().getMtCtrlBusiFieldMap().get(jkbxvo.getParentVO().getPk_item());
    boolean ismashare = (jkbxvo.getParentVO().getIsmashare() != null) && (jkbxvo.getParentVO().getIsmashare().booleanValue());

    if (isBX()) {
      checkCtrlField(ctrlFieldList, ismashare);
    }

    setHeadFieldNotEdit();

    if ((jkbxvo.getChildrenVO() != null) && (jkbxvo.getChildrenVO().length > 0))
    {
      int index = getEditor().getBillCardPanel().getBillData().getBodyTableCodeIndex(getBusPageCode());
      getEditor().getBillCardPanel().getBodyTabbedPane().setSelectedIndex(index);

      if ("bx".equals(jkbxvo.getParentVO().getDjdl())) {
        Integer paytarget = (Integer)getEditor().getBillCardPanel().getHeadItem("paytarget").getValueObject();
        for (int i = 0; i < jkbxvo.getChildrenVO().length; i++)
          getEditor().getBillCardPanel().setBodyValueAt(paytarget, i, "paytarget");
      }
    }
  }

  public void resetBillItemOnEdit()
    throws BusinessException
  {
    setHeadFieldNotEdit();
  }

  private void setHeadFieldNotEdit()
  {
    ((ErmBillBillForm)getEditor()).getBillOrgPanel().getRefPane().setEnabled(false);
    getEditor().getBillCardPanel().getHeadItem("pk_org_v").setEnabled(false);
    getEditor().getBillCardPanel().getHeadItem("pk_org").setEnabled(false);
    getEditor().getBillCardPanel().getHeadItem("bzbm").setEnabled(false);
    getEditor().getBillCardPanel().getHeadItem(JKBXHeaderVO.FYDWBM_V).setEnabled(false);
    getEditor().getBillCardPanel().getHeadItem("fydwbm").setEnabled(false);

    BillItem ismashare = getEditor().getBillCardPanel().getHeadItem("ismashare");
    if ((ismashare != null) && (((Boolean)ismashare.getValueObject()).booleanValue())) {
      BillItem iscostshareItem = getEditor().getBillCardPanel().getHeadItem("iscostshare");
      if (iscostshareItem != null)
        iscostshareItem.setEnabled(false);
    }
  }

  private void modifyOtherJeOfBody() throws BusinessException
  {
    JKBXVO aggvo = (JKBXVO)this.billform.getResVO().getBusiobj();
    BXBusItemVO[] busitemVOs = aggvo.getBxBusItemVOS();

    String[] bodyTableCodes = getEditor().getBillCardPanel().getBillData().getBodyTableCodes();
    if (bodyTableCodes != null) {
      getEditor().getBillCardPanel().getBillModel(bodyTableCodes[0]).setNeedCalculate(false);
    }

    for (int i = 0; i < busitemVOs.length; i++) {
      Object amount = getEditor().getBillCardPanel().getBodyValueAt(i, "amount");
      getEditor().getBillCardPanel().setBodyValueAt(amount, i, "ybje");
      new BodyEventHandleUtil((ErmBillBillForm)getEditor()).modifyFinValues("ybje", i, null);
    }

    if (bodyTableCodes != null) {
      getEditor().getBillCardPanel().getBillModel(bodyTableCodes[0]).setNeedCalculate(true);
    }

    ((ErmBillBillForm)getEditor()).getbodyEventHandle().calcuateHeadJe();
  }

  private void checkCtrlField(List<String> ctrlFieldList, boolean ismashare)
    throws BusinessException
  {
    if ((ctrlFieldList == null) || (ctrlFieldList.size() < 0)) {
      return;
    }
    Map map = groupCtrlFieldCodeMap(ctrlFieldList);

    checkHeadCtrlFieldIsShow((List)map.get("head"));

    if (ismashare)
    {
      checkCSCtrlFieldIsShow((List)map.get("costsharedetail"));
    }
    else checkBusCtrlFieldIsShow((List)map.get("bus_body")); 
  }

  private void checkBusCtrlFieldIsShow(List<String> ctrlfields)
    throws BusinessException
  {
    if ((ctrlfields == null) || (ctrlfields.size() < 0)) {
      return;
    }
    boolean flag = false;
    for (String ctrlfield : ctrlfields) {
      String[] keys = StringUtil.split(ctrlfield, ".");
      BillItem item = getEditor().getBillCardPanel().getBodyItem(getBusPageCode(), keys[1]);
      if ((item == null) || (!item.isShow())) {
        flag = true;
        break;
      }
    }
    if (flag)
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("201107_0", "0201107-0155"));
  }

  private void checkCSCtrlFieldIsShow(List<String> ctrlfields)
    throws BusinessException
  {
    if ((ctrlfields == null) || (ctrlfields.size() < 0)) {
      return;
    }
    boolean flag = false;
    for (String ctrlfield : ctrlfields) {
      String[] keys = StringUtil.split(ctrlfield, ".");
      BillItem item = getEditor().getBillCardPanel().getBodyItem("er_cshare_detail", keys[1]);
      if ((item == null) || (!item.isShow())) {
        flag = true;
        break;
      }
    }
    if (flag)
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("201107_0", "0201107-0155"));
  }

  private void checkHeadCtrlFieldIsShow(List<String> ctrlfields)
    throws BusinessException
  {
    if ((ctrlfields == null) || (ctrlfields.size() < 0)) {
      return;
    }
    boolean flag = false;
    for (String ctrlfield : ctrlfields)
    {
      String fieldcode_v = null;
      if (JKBXHeaderVO.getOrgMultiVersionFieldMap().containsKey(ctrlfield)) {
        fieldcode_v = JKBXHeaderVO.getOrgVFieldByField(ctrlfield);
      }
      BillItem item = getEditor().getBillCardPanel().getHeadItem(ctrlfield);
      BillItem item_v = getEditor().getBillCardPanel().getHeadItem(fieldcode_v);
      if (((item == null) || (!item.isShow())) && ((item_v == null) || (!item_v.isShow()))) {
        flag = true;
        break;
      }
    }

    if (flag)
      throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("201107_0", "0201107-0155"));
  }

  private Map<String, List<String>> groupCtrlFieldCodeMap(List<String> fieldcodes)
  {
    Map map = new HashMap();
    List cShareCodes = new ArrayList();
    List busCodes = new ArrayList();
    List headCodes = new ArrayList();
    for (String fieldcode : fieldcodes) {
      if (fieldcode.indexOf(46) != -1) {
        String[] keys = StringUtil.split(fieldcode, ".");
        if ("costsharedetail".equals(keys[0]))
          cShareCodes.add(fieldcode);
        else
          busCodes.add(fieldcode);
      }
      else {
        headCodes.add(fieldcode);
      }
    }
    map.put("head", headCodes);
    map.put("bus_body", busCodes);
    map.put("costsharedetail", cShareCodes);
    return map;
  }

  private String getBusPageCode()
  {
    if (isBX()) {
      return "arap_bxbusitem";
    }
    return "jk_busitem";
  }

  private boolean isBX() {
    String currentBillTypeCode = ((ErmBillBillManageModel)getEditor().getModel()).getCurrentBillTypeCode();
    DjLXVO currentDjlx = ((ErmBillBillManageModel)getEditor().getModel()).getCurrentDjlx(currentBillTypeCode);
    return "bx".equals(currentDjlx.getDjdl());
  }

  private BillForm getEditor() {
    return this.billform;
  }
}