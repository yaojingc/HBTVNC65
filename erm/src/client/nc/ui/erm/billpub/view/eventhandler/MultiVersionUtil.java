package nc.ui.erm.billpub.view.eventhandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.er.util.BXUiUtil;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.vorg.ref.AdminOrgVersionDefaultRefModel;
import nc.ui.vorg.ref.BusinessUnitVersionDefaultRefModel;
import nc.ui.vorg.ref.DeptVersionDefaultRefModel;
import nc.ui.vorg.ref.FinanceOrgVersionDefaultRefTreeModel;
import nc.ui.vorg.ref.LiabilityCenterVersionDefaultRefModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.AbstractNCLangRes;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.BusinessRuntimeException;
import nc.vo.pub.lang.UFDate;

public class MultiVersionUtil
{
  public static void setHeadOrgMultiVersion(String field, String pk_value, BillCardPanel cardPanel, ErmBillBillForm editor)
    throws BusinessException
  {
    if (StringUtil.isEmpty(pk_value)) {
      return;
    }
    UFDate date = (UFDate)cardPanel.getHeadItem("djrq").getValueObject();

    if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
      date = BXUiUtil.getBusiDate();
    }

    String pk_vid = getBillHeadFinanceOrgVersion(field, pk_value, date, cardPanel);

    BillItem headItem = cardPanel.getHeadItem(field);
    if (headItem != null)
      headItem.setValue(pk_vid);
  }

  public static void setBodyOrgMultiVersion(String field_v, String field, ErmBillBillForm editor)
    throws BusinessException
  {
    if ((StringUtil.isEmpty(field_v)) || (StringUtil.isEmpty(field))) {
      return;
    }

    UFDate date = (UFDate)editor.getBillCardPanel().getHeadItem("djrq").getValueObject();

    if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
      date = BXUiUtil.getBusiDate();
    }

    String[] tableCodes = editor.getBillCardPanel().getBillData().getBodyTableCodes();
    for (String tableCode : tableCodes)
      if ((!"er_cshare_detail".equals(tableCode)) && (!"er_bxcontrast".equals(tableCode)))
      {
        BillItem item_v = editor.getBillCardPanel().getBodyItem(tableCode, field_v);
        BillItem item = editor.getBillCardPanel().getBodyItem(tableCode, field);

        UIRefPane refPane_v = null;
        if ((item != null) && (item_v != null) && ((item_v.getComponent() instanceof UIRefPane))) {
          refPane_v = (UIRefPane)item_v.getComponent();

          int rowCount = editor.getBillCardPanel().getBillModel(tableCode).getRowCount();
          for (int i = 0; i < rowCount; i++) {
            String pk = null;
            Object value = editor.getBillCardPanel().getBillModel(tableCode).getValueObjectAt(i, field);
            if (value != null) {
              if ((value instanceof String))
                pk = (String)value;
              else if ((value instanceof DefaultConstEnum)) {
                pk = (String)((DefaultConstEnum)value).getValue();
              }
              Map map = getFinanceOrgVersion(refPane_v.getRefModel(), new String[] { pk }, date);

              String vid = map.keySet().size() == 0 ? null : (String)map.keySet().iterator().next();
              editor.getBillCardPanel().getBillModel(tableCode).setValueAt(vid, i, field_v + "_ID");
              editor.getBillCardPanel().getBillModel(tableCode).loadLoadRelationItemValue(i, field_v);
            }
          }
        }
      }
  }

  public static void setBodyOrgValueByVersion(String field_v, String field, ErmBillBillForm editor)
    throws BusinessException
  {
    if ((StringUtil.isEmpty(field_v)) || (StringUtil.isEmpty(field))) {
      return;
    }

    UFDate date = (UFDate)editor.getBillCardPanel().getHeadItem("djrq").getValueObject();

    if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
      date = BXUiUtil.getBusiDate();
    }

    String[] tableCodes = editor.getBillCardPanel().getBillData().getBodyTableCodes();
    for (String tableCode : tableCodes)
      if ((!"er_cshare_detail".equals(tableCode)) && (!"er_bxcontrast".equals(tableCode)))
      {
        BillItem item_v = editor.getBillCardPanel().getBodyItem(tableCode, field_v);
        BillItem item = editor.getBillCardPanel().getBodyItem(tableCode, field);

        UIRefPane refPane_v = null;
        if ((item != null) && (item_v != null) && ((item_v.getComponent() instanceof UIRefPane))) {
          refPane_v = (UIRefPane)item_v.getComponent();

          int rowCount = editor.getBillCardPanel().getBillModel(tableCode).getRowCount();
          for (int i = 0; i < rowCount; i++) {
            String pk = null;
            Object value = editor.getBillCardPanel().getBillModel(tableCode).getValueObjectAt(i, field);
            if (value != null) {
              if ((value instanceof String))
                pk = (String)value;
              else if ((value instanceof DefaultConstEnum)) {
                pk = (String)((DefaultConstEnum)value).getValue();
              }
              Map map = getFinanceOrgVersion(refPane_v.getRefModel(), new String[] { pk }, date);

              String vid = map.keySet().size() == 0 ? null : (String)map.keySet().iterator().next();
              editor.getBillCardPanel().getBillModel(tableCode).setValueAt(vid, i, field_v + "_ID");
              editor.getBillCardPanel().getBillModel(tableCode).loadLoadRelationItemValue(i, field_v);
            }
          }
        }
      }
  }

  public static String getBillHeadFinanceOrgVersion(String orgHeadItemKey, String oid, UFDate vstartdate, BillCardPanel cardPanel)
  {
    if (cardPanel.getHeadItem(orgHeadItemKey) == null) {
      throw new BusinessRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011ermpub0316_0", "02011ermpub0316-0003"));
    }

    UIRefPane refPane = (UIRefPane)cardPanel.getHeadItem(orgHeadItemKey).getComponent();

    Map map = getFinanceOrgVersion(refPane.getRefModel(), new String[] { oid }, vstartdate);

    String vid = map.keySet().size() == 0 ? null : (String)map.keySet().iterator().next();
    return vid;
  }

  public static Map<String, String> getFinanceOrgVersion(AbstractRefModel versionModel, String[] oids, UFDate vstartdate)
  {
    if ((versionModel instanceof FinanceOrgVersionDefaultRefTreeModel)) {
      FinanceOrgVersionDefaultRefTreeModel model = (FinanceOrgVersionDefaultRefTreeModel)versionModel;

      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_financeorg", oids, "pk_vid");
    }if ((versionModel instanceof BusinessUnitVersionDefaultRefModel)) {
      BusinessUnitVersionDefaultRefModel model = (BusinessUnitVersionDefaultRefModel)versionModel;
      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_org", oids, "pk_vid");
    }
    if ((versionModel instanceof LiabilityCenterVersionDefaultRefModel)) {
      LiabilityCenterVersionDefaultRefModel model = (LiabilityCenterVersionDefaultRefModel)versionModel;
      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_liabilitycenter", oids, "pk_vid");
    }
    if ((versionModel instanceof AdminOrgVersionDefaultRefModel)) {
      AdminOrgVersionDefaultRefModel model = (AdminOrgVersionDefaultRefModel)versionModel;
      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_adminorg", oids, "pk_vid");
    }if ((versionModel instanceof DeptVersionDefaultRefModel)) {
      DeptVersionDefaultRefModel model = (DeptVersionDefaultRefModel)versionModel;
      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_dept", oids, "pk_vid");
    }
    return new HashMap();
  }

  public static String getBillFinanceOrg(AbstractRefModel versionModel, String vid)
  {
    if ((versionModel instanceof FinanceOrgVersionDefaultRefTreeModel)) {
      FinanceOrgVersionDefaultRefTreeModel model = (FinanceOrgVersionDefaultRefTreeModel)versionModel;
      model.matchPkData(vid);
      Object value = model.getValue("pk_financeorg");
      return (String)value;
    }if ((versionModel instanceof LiabilityCenterVersionDefaultRefModel)) {
      LiabilityCenterVersionDefaultRefModel model = (LiabilityCenterVersionDefaultRefModel)versionModel;
      model.matchPkData(vid);
      Object value = model.getValue("pk_liabilitycenter");

      return (String)value;
    }if ((versionModel instanceof BusinessUnitVersionDefaultRefModel)) {
      BusinessUnitVersionDefaultRefModel model = (BusinessUnitVersionDefaultRefModel)versionModel;
      model.matchPkData(vid);
      Object value = model.getValue("pk_org");
      return (String)value;
    }if ((versionModel instanceof AdminOrgVersionDefaultRefModel)) {
      AdminOrgVersionDefaultRefModel model = (AdminOrgVersionDefaultRefModel)versionModel;
      model.matchPkData(vid);
      Object value = model.getValue("pk_adminorg");
      return (String)value;
    }
    return null;
  }

  private static Map<String, String> getRefModelMatchMap(AbstractRefModel model, String matchField, String[] matchValues, String matchedField)
  {
    if ((model instanceof FinanceOrgVersionDefaultRefTreeModel))
      model.setDataPowerOperation_code("fi");
    Map map = new HashMap();
    model.setIsRefreshMatch(false);
    Vector matchData = model.matchData(matchField, matchValues);
    if (matchData != null) {
      Iterator it = matchData.iterator();
      int oid_idx = model.getFieldIndex(matchField);
      int vid_idx = model.getFieldIndex(matchedField);
      while (it.hasNext()) {
        Vector next = (Vector)it.next();
        String pk_vid = (String)next.get(vid_idx);
        String pk_oid = (String)next.get(oid_idx);
        map.put(pk_vid, pk_oid);
      }
    }
    return map;
  }

  public static void setHeadDeptMultiVersion(String field, String pk_org, String pk_dept, BillCardPanel billCardPanel, boolean isQc)
    throws BusinessException
  {
    UFDate date = (UFDate)billCardPanel.getHeadItem("djrq").getValueObject();

    if ((date == null) || (StringUtil.isEmpty(date.toString()))) {
      date = BXUiUtil.getBusiDate();
    }

    billCardPanel.getHeadItem(field).setValue(getBillHeadDeptVersion(field, pk_org, pk_dept, date, billCardPanel));
  }

  private static String getBillHeadDeptVersion(String headDeptItemKey, String pk_org, String oid, UFDate vstartdate, BillCardPanel billCardPanel)
  {
    if (billCardPanel.getHeadItem(headDeptItemKey) == null) {
      throw new BusinessRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011ermpub0316_0", "02011ermpub0316-0002"));
    }

    UIRefPane refPane = (UIRefPane)billCardPanel.getHeadItem(headDeptItemKey).getComponent();
    Map map = getDeptVersion(refPane.getRefModel(), pk_org, new String[] { oid }, vstartdate);

    String vid = null;
    if (map.size() > 0) {
      vid = (String)map.keySet().iterator().next();
    }
    return vid;
  }

  public static String getDeptVersion(AbstractRefModel versionModel, String pk_org, String oid, UFDate vstartdate)
  {
    Map map = getDeptVersion(versionModel, pk_org, new String[] { oid }, vstartdate);
    String vid = (String)map.keySet().iterator().next();
    return vid;
  }

  private static Map<String, String> getDeptVersion(AbstractRefModel versionModel, String pk_org, String[] oids, UFDate vstartdate)
  {
    if ((versionModel instanceof DeptVersionDefaultRefModel)) {
      DeptVersionDefaultRefModel model = (DeptVersionDefaultRefModel)versionModel;
      model.setPk_org(pk_org);
      model.setVstartdate(vstartdate);
      return getRefModelMatchMap(model, "pk_dept", oids, "pk_vid");
    }
    return new HashMap();
  }
}