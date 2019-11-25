package nc.ui.erm.billpub.view.eventhandler;

import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.bill.BillCardPanel;


/**
 * @ClassName: HeadAfterEditHandler  
 * @Description: ����ѱ����� ��ͷ�༭���¼���������װ��
 * @author yaojing
 * @date 2019��10��29�� ����2:33:31
 */
public class HeadAfterEditHandler {
	
	private ErmBillBillForm editor = null;

	public HeadAfterEditHandler(ErmBillBillForm editor) {
		this.editor = editor;
	}
	
	/**
	* @Title: doEdit  
	* @Description: ���崦��༭�¼���ֵ�ķ���
	* @param @param editField  �����������ڱ༭���ֶ���
	* @return void    ��������  
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
