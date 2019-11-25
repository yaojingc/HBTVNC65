package nc.ui.erm.billpub.view.eventhandler;

import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.workflow.util.ERMCrossCheckUtil;
import nc.ui.uif2.editor.BillForm;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.er.djlx.DjLXVO;
import nc.vo.pub.BusinessException;

public class CrossCheckUtil
{
  public static void checkRule(String headOrBody, String key, BillForm editor)
    throws BusinessException
  {
    String currentBillTypeCode = ((ErmBillBillManageModel)editor.getModel()).getCurrentBillTypeCode();
    DjLXVO currentDjlx = ((ErmBillBillManageModel)editor.getModel()).getCurrentDjlx(currentBillTypeCode);
    String djdl = currentDjlx.getDjdl();

    String parentBilltype = "bx".equals(djdl) ? "264X" : "263X";

    String[] orgFields = { "fydwbm", JKBXHeaderVO.PK_PAYORG, "dwbm", "pk_org" };
    ERMCrossCheckUtil.checkRule(headOrBody, key, editor, currentBillTypeCode, parentBilltype, orgFields);
  }
}