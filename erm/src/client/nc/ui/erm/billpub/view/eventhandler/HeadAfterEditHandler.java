package nc.ui.erm.billpub.view.eventhandler;

import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.bill.BillCardPanel;


/**
 * @ClassName: HeadAfterEditHandler  
 * @Description: 劳务费报销单 表头编辑后事件（单独封装）
 * @author yaojing
 * @date 2019年10月29日 下午2:33:31
 */
public class HeadAfterEditHandler {
	
	private ErmBillBillForm editor = null;

	public HeadAfterEditHandler(ErmBillBillForm editor) {
		this.editor = editor;
	}
	
	/**
	* @Title: doEdit  
	* @Description: 具体处理编辑事件赋值的方法
	* @param @param editField  传进来的正在编辑的字段名
	* @return void    返回类型  
	* @throws
	 */
	public void doEdit(String editField){
		
		
		
		
		System.out.println("1111");
	}
	
	protected void setHeadValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null)
			getBillCardPanel().getHeadItem(key).setValue(value);
	}
	
	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}
	
}
