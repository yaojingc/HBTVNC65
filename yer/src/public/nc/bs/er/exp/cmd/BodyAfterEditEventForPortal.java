package nc.bs.er.exp.cmd;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.uap.lfw.core.AppInteractionUtil;
import nc.uap.lfw.core.LfwRuntimeEnvironment;
import nc.uap.lfw.core.comp.FormComp;
import nc.uap.lfw.core.comp.FormElement;
import nc.uap.lfw.core.comp.GridColumn;
import nc.uap.lfw.core.comp.GridComp;
import nc.uap.lfw.core.ctx.AppLifeCycleContext;
import nc.uap.lfw.core.data.Dataset;
import nc.uap.lfw.core.data.Row;
import nc.uap.lfw.core.page.LfwView;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.IdCardUtil;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillItem;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

public class BodyAfterEditEventForPortal {
	
	/**
	 * 
	 * @Title: doEdit
	 * @Description: ���崦��༭�¼���ֵ�ķ���
	 * @param @param editField �����������ڱ༭���ֶ���
	 * @return void ��������
	 * @throws
	 */
	public void doEdit(String editField) {
		LfwView mainWidget = AppLifeCycleContext.current().getWindowContext()
				.getViewContext("main").getView();
		// �õ���ͷ���ݼ�
		Dataset mailds = mainWidget.getViewModels().getDataset("bxzb");
		// �õ��������ݼ�
		Dataset bodyData = mainWidget.getViewModels().getDataset("busitem");
		// �õ���ǰ���ڱ༭���ж���
		Row row = bodyData.getSelectedRow();
		// �õ���ǰ���ڱ༭���к�
		Integer index = bodyData.getSelectedIndex();
		
		// ��ȡ��ǰportal������ѽڵ�Ľ�������
		String transType = mailds.getSelectedRow().getString(mailds.nameToIndex("djlxbm"));
		if (ServiceFeeTaxUtils.TRANSTYPE.equals(transType)) {
			switch (editField) {
			case "defitem50":
				// У��1����֤��������ͣ����л����������ʱ��������ֵ
				this.check_01(bodyData,row);

				break;

			case "defitem37":
				// У��2�� ��֤�ֻ�����
				this.check_02(bodyData,row,editField);

				break;

			case "defitem42":
				// У��2_1�������Ϣ
				this.check_02(bodyData,row,editField);
				// У��7��UAP��Ա����״̬Ϊ���á���֯Ϊ̨��������Ա���Ϊxxʱ�����ݵ����е����֤����ʶ�𣬲��������ѱ�����У�顣
				this.check_07(bodyData,row,index);
				break;

			case "defitem36":
				// У��3����֤�Ƿ����ֳ����� �ֶ�
				this.check_03(editField, 1);

				break;

			case "defitem48":
				// У��4����֤���������֤�����һ����д������ʱҪ��ƥ��
				this.check_04(bodyData,row);

				break;
			case "defitem43":
				// У��5���л�֤��������Ӧ����������
				this.check_05(bodyData,row);

				break;

			case "szxmid":
				// У��6���л���֧��Ŀ�� ��� �����ֵ
				this.check_06(bodyData,row);

				break;

			case "amount":
				// ����1 : ����Ӧ�������� ����Ԥ�ƿ�˰ ��ʵ�����
				this.setValue_01(bodyData,row);

				break;

			case "defitem44":
				// ����2 : ����ʵ�������� ����Ԥ�ƿ�˰ ��Ӧ�����
				this.setValue_02(bodyData,row);

				break;

			default:
				break;
			}
		}		
		

	}
	
	
	
	/**
	 * 1.��Ϊ˰ǰ����ʱ��ֻ�ܱ༭Ӧ����� 2.��Ϊ˰������ʱ��ֻ�ܱ༭ʵ�����
	 * amount Ӧ����� 
	 * defitem50 ��������� 
	 * defitem44 ʵ�����
	 */
	private void check_01(Dataset bodyData,Row row) {
		// �õ� defitem50 ���������
		String defitem50 =  (String) row.getValue(bodyData.nameToIndex("defitem50"));
		// ����ת��
		String serviceType = HRATCommonUtils.queryNameByDefdocPk(defitem50,"���������");
		
		if (MMValueCheck.isNotEmpty(serviceType)) {
			// ����ǰ�б�������������Ϊ˰ǰ����
			if ("˰ǰ����".equals(serviceType)) {// Ӧ���ɱ༭��ʵ�����ɱ༭
				YerUtil.setFormEleEdit("busitem_grid", new String[]{"amount"});
				YerUtil.setFormEleUnEdit("busitem_grid", new String[]{"defitem44"});
			} else {// Ӧ�����ɱ༭��ʵ���ɱ༭
				YerUtil.setFormEleEdit("busitem_grid", new String[]{"defitem44"});
				YerUtil.setFormEleUnEdit("busitem_grid", new String[]{"amount"});
			}
		}
		// Ӧ�����
		row.setValue(bodyData.nameToIndex("amount"),new UFDouble(0));
		// ʵ�����
		row.setValue(bodyData.nameToIndex("defitem44"),new UFDouble(0));
		// ����Ԥ�ƿ�˰��defitem46
		row.setValue(bodyData.nameToIndex("defitem46"),"");
		// ������˰��defitem45
		row.setValue(bodyData.nameToIndex("defitem45"),"");
		// �ۼ�Ӧ�� defitem47
		row.setValue(bodyData.nameToIndex("defitem47"),"");
	}
	
	
	/**
	 * ��֤�ֻ����룬�����Ϣ
	 * 
	 * �ֻ����� defitem37 
	 * ֤������ defitem42
	 * 
	 * 2.��д���������֤��֮���Զ����� �Ա𣬳������ڣ���������
	 * 
	 * �������� �������� defitem38
	 * �������� defitem39
	 * �Ա� defitem40
	 * �������� defitem41
	 * ֤������ defitem42
	 * ֤������ defitem43    
	 */
	private void check_02(Dataset bodyData,Row row,String editField) {
		// �õ�ǰ̨��д���ֻ�����
		String phoneNumber = (String) row.getValue(bodyData.nameToIndex("defitem37"));
		// �õ�ǰ̨��д��֤������
		String pk_idCardType = (String) row.getValue(bodyData.nameToIndex("defitem43"));
		// ֤�����Ͷ���ת��
		String idCardType = HRATCommonUtils.queryNameByDefdocPk(pk_idCardType,"֤������");
		// �õ�ǰ̨��д�����֤����
		String idCardNo = (String) row.getValue(bodyData.nameToIndex("defitem42"));

		// ����ǰ�༭���ֶ�Ϊ defitem37 �ֻ�����
		if ("defitem37".equals(editField)) {
			if (MMValueCheck.isNotEmpty(phoneNumber)) {
				if (phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
					AppInteractionUtil.showMessageDialog("��ǰ�У��ֻ�������д���󣬳���ӦΪ11λ������1��ͷ��", "����", "ȷ��");
					// ��֮ǰ��д������ֻ�������յ�
					row.setValue(bodyData.nameToIndex("defitem37"),"");
				}
			}
		}

		// ����ǰ�༭���ֶ�Ϊ defitem42 ֤������
		if ("defitem42".equals(editField)) {
			if (MMValueCheck.isNotEmpty(idCardNo)) {
				// ��֤������Ϊ���֤ʱ������У�飬����ȡ���֤�е���Ϣ��ֵ
				if ("���֤".equals(idCardType)) {
					// У�����֤�� Start
					boolean isIdOjbk = IdCardUtil.isValidatedAllIdcard(idCardNo);
					if (!isIdOjbk) {
						AppInteractionUtil.showMessageDialog("��ǰ�У����֤�� �� " + idCardNo + "   ���Ϸ�,��˶� ��", "����", "ȷ��");
						// ��֮ǰ��д��������֤����յ�
						row.setValue(bodyData.nameToIndex("defitem42"),"");
					} else {
						// У�����֤��ͨ��֮�󣬸����漸���ֶθ�ֵ
						// ����������ֵ����֤���������֤ʱ��Ĭ��Ϊ�й�
						row.setValue(bodyData.nameToIndex("defitem41"),HRATCommonUtils.queryDefdocPk("�й�", "����"));
						// ���Ա�ֵ
						row.setValue(bodyData.nameToIndex("defitem40"),IdCardUtil.getSexFromidCardNo(idCardNo));
						// ���������ڸ�ֵ
						row.setValue(bodyData.nameToIndex("defitem39"),IdCardUtil.getBirthDayFromidCardNo(idCardNo));
						// ���������� ����������ֵ
						row.setValue(bodyData.nameToIndex("defitem38"),HRATCommonUtils.queryDefdocPk("�й�", "����"));
					}
				} else {
					// ��֤�����Ͳ������֤��ʱ�������Ӧ����ͨ�������������� idCardType��֤������ idCardNo����У�飬
					boolean flag = HRATCommonUtils.judgeIDNumberIsLegal(idCardType, idCardNo);
					if (flag) {
						AppInteractionUtil.showMessageDialog("��ǰ�У�֤������ �� " + idCardNo + "   ���Ϸ�,��˶� ��", "����", "ȷ��");
						// ��֮ǰ��д��������֤����յ�
						row.setValue(bodyData.nameToIndex("defitem42"),"");
					}
				}
			} else {
				// ��������ֵΪ��
				row.setValue(bodyData.nameToIndex("defitem41"),"");
				// ���Ա�ֵΪ��
				row.setValue(bodyData.nameToIndex("defitem40"),"");
				// ���������ڸ�ֵΪ��
				row.setValue(bodyData.nameToIndex("defitem39"),"");
				// ���������� ����������ֵΪ��
				row.setValue(bodyData.nameToIndex("defitem38"),"");
			}
		}
	}
	
	
	
	/**
	 * UAP��Ա����״̬Ϊ���á���֯Ϊ̨���������ݵ����е����֤����ʶ�𣬲��������ѱ�����У�顣��Ա���Ϊ�����ݡ���ְ���Ա�������ѣ�
	 */
	private void check_07(Dataset bodyData,Row row,Integer index) {
		// �õ�ǰ̨��д��֤������
		String idCardNo = (String) row.getValue(bodyData.nameToIndex("defitem42"));
		// �õ�ǰ̨��д������
		String name = (String) row.getValue(bodyData.nameToIndex("defitem48"));
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		SqlBuilder sql = new SqlBuilder();
		// ����д�����֤�ź���������Ϊ�յ�ʱ��ִ��У��
		if (HRATCommonUtils.isNotEmpty(idCardNo) && HRATCommonUtils.isNotEmpty(name)) {
			sql.append("select name, id from BD_PSNDOC where PK_PSNDOC in(select PK_PSNDOC from HI_PSNJOB where PK_PSNCL in (select pk_psncl from BD_PSNCL where NAME in ('ʵ����', '������', '��ǲ��', '��ҵ��Ƹ', 'ת�����', '����', '����')))");
			sql.append(" and name='");
			sql.append(name);
			sql.append("'");
			sql.append(" and id='");
			sql.append(idCardNo);
			sql.append("'");
			sql.append(" and enablestate=2");	//����״̬ �Ƿ�����
			sql.append(" and PK_ORG =(select PK_ORG from org_orgs where  name = '�����㲥����̨̨����')");//̨����У��
			try {
				Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());
				// ������ݿ����ܹ���ѯ������,˵�����û�������,�ÿ���Ӧ�ֶ�
				if (result != null) {
					AppInteractionUtil.showMessageDialog( "��" + (index + 1) + " �е���Ա����ְ��Ա,������������ѣ���˶Ժ���д��", "����", "ȷ��");
					row.setValue(bodyData.nameToIndex("defitem38"),"");
					row.setValue(bodyData.nameToIndex("defitem39"),"");
					row.setValue(bodyData.nameToIndex("defitem40"),"");
					row.setValue(bodyData.nameToIndex("defitem41"),"");
					row.setValue(bodyData.nameToIndex("defitem42"),"");
					row.setValue(bodyData.nameToIndex("defitem48"),"");
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		} else {
//			AppInteractionUtil.showMessageDialog( "��ǰ��,���������֤�Ų���Ϊ�գ���˶Ժ���д��", "����", "ȷ��");
		}
	}


	/**
	 * �Ƿ����ֳ����ţ����գ�ѡ��Y��N�����ѡ��N����������𡢿������С������˻���Ҫ��� �Ƿ����ֳ����� defitem36 �������
	 * defitem35 �������� defitem34 ���п��� defitem33
	 */
	private void check_03(String editField, int row) {
		// �õ�ǰ̨ѡ��� �Ƿ��ֳ����� �ֶ�ֵ
//		String isSitetoExtend = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem36");

//		BillItem bankType = editor.getBillCardPanel().getBodyItem("defitem35");
//		BillItem bankName = editor.getBillCardPanel().getBodyItem("defitem34");
//		BillItem bankNo = editor.getBillCardPanel().getBodyItem("defitem33");
//
//		if ("��".equals(isSitetoExtend)) {
//			bankType.setNull(true);
//			bankName.setNull(true);
//			bankNo.setNull(true);
//		} else {
//			bankType.setNull(false);
//			bankName.setNull(false);
//			bankNo.setNull(false);
//		}
	}

	
	/**
	 * @Title: check_04
	 * @Description: ��д����ʱУ������֤�ŵ�һ����д������ʱ��Ӧ�������Ƿ�һ��
	 * 
	 *               ���� defitem48 ���� ������er_busitem
	 * @param @param editField
	 * @param @param row ����
	 * @return void ��������
	 * @throws
	 */
	private void check_04(Dataset bodyData,Row row) {
		// �õ�ǰ̨��д�����֤����
		String idCardNo = (String) row.getValue(bodyData.nameToIndex("defitem42"));
		// �õ�ǰ̨��д������
		String name = (String) row.getValue(bodyData.nameToIndex("defitem48"));

		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		SqlBuilder sql = new SqlBuilder();

		// ����д�����֤�ź���������Ϊ�յ�ʱ��ִ��У��
		if (HRATCommonUtils.isNotEmpty(idCardNo) && HRATCommonUtils.isNotEmpty(name)) {
			sql.append("    SELECT tb_b.defitem48 ");
			sql.append("      FROM er_busitem tb_b ");
			sql.append(" LEFT JOIN er_bxzb tb_h ");
			sql.append(" 		ON ");
			sql.append(" tb_h.pk_jkbx = tb_b.pk_jkbx ");
			sql.append(" 	 WHERE ");
			sql.append("tb_h.djlxbm", ServiceFeeTaxUtils.TRANSTYPE);
			sql.append(" 	   AND ");
			sql.append(" tb_b.defitem42 ", idCardNo);
			sql.append("  ORDER BY tb_b.ts ASC ");

			try {
				Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());

				if (HRATCommonUtils.isNotEmpty(result)) {
					String nameOril = result.toString();
					if (!nameOril.equals(name)) {
						AppInteractionUtil.showMessageDialog("��ǰ��,���� �� " + name + "  ��ԭʼ���������ƥ�䣬��˶Ժ���д��", "����", "ȷ��");
						// ��֮ǰ��д��������֤����յ�
						row.setValue(bodyData.nameToIndex("defitem48"),"");
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else{
//			AppInteractionUtil.showMessageDialog("��ǰ�����������֤��Ϊ�գ�����д��", "����", "ȷ��");
		}
	}
	
	
	/**
	 * @Title: check_05
	 * @Description: �л�֤������,��Ӧ����������
	 * 
	 *               ֤������ defitem43 ���� ������er_busitem
	 * @param @param editField
	 * @param @param row ����
	 * @return void ��������
	 * @throws
	 */
	private void check_05(Dataset bodyData,Row row) {
		row.setValue(bodyData.nameToIndex("defitem38"),"");
		row.setValue(bodyData.nameToIndex("defitem39"),"");
		row.setValue(bodyData.nameToIndex("defitem40"),"");
		row.setValue(bodyData.nameToIndex("defitem41"),"");
		row.setValue(bodyData.nameToIndex("defitem42"),"");
	}

	/**
	 * ѡ����֧��Ŀ֮����ܱ༭��Ӧ�Ľ�������Ӧ�Ĺ�ʽ
	 * 
	 * @param editField
	 * @param row
	 *            ʵ����defitem44 Ӧ����amount ����Ԥ�ƿ�˰��defitem46 ������˰��defitem45
	 *            �ۼ�Ӧ�� defitem47
	 */
	private void check_06(Dataset bodyData,Row row) {
		// Ӧ�����
		row.setValue(bodyData.nameToIndex("amount"),new UFDouble(0));
		// ʵ�����
		row.setValue(bodyData.nameToIndex("defitem44"),new UFDouble(0));
		// ����Ԥ�ƿ�˰��defitem46
		row.setValue(bodyData.nameToIndex("defitem46"),null);
		// ������˰��defitem45
		row.setValue(bodyData.nameToIndex("defitem45"),null);
		// �ۼ�Ӧ�� defitem47
		row.setValue(bodyData.nameToIndex("defitem47"),null);
	}


	/**
	 * �õ�Ӧ�������������Ԥ�ƿ�˰ �� ʵ�����
	 * 
	 * Ӧ����� ��amount ����Ԥ�ƿ�˰ �� defitem46 ʵ����� : defitem44 ��֧��Ŀ:szxmid
	 */
	private void setValue_01(Dataset bodyData,Row row) {
		// ��ȡ��֧��Ŀ��ֵ
		String pk_incomeType = (String) row.getValue(bodyData.nameToIndex("szxmid"));
		// ��֧��Ŀ����ת��
		String incomeType = HRATCommonUtils.queryInoutbusiclassByName(pk_incomeType);
		// �õ� amount Ӧ�����
		Double amountBefore = Double.parseDouble(row.getValue(bodyData.nameToIndex("amount")).toString());
		if (amountBefore < 0) {
			// ������븺ֵ���� ,������ʾ��Ϣ,���������Ӧ��������
			AppInteractionUtil.showMessageDialog("��������ȷ���,Ӧ������Ϊ��ֵ!", "����", "ȷ��");
			row.setValue(bodyData.nameToIndex("amount"),new UFDouble(0));
			row.setValue(bodyData.nameToIndex("defitem44"),new UFDouble(0));
			row.setValue(bodyData.nameToIndex("defitem46"),"");
		} else if (amountBefore == 0){
			// nothing TODO
		} else {
			// ������֧��Ŀ������������ʵ�����
			Double amountAfter = 0D;
			
			if(HRATCommonUtils.isNotEmpty(incomeType)){
				if (incomeType.contains("�����")) {
					amountAfter = ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(amountBefore);
					// ����Ԥ�ƿ�˰
					Double tax = amountBefore - amountAfter;
					// ��ҳ�����ʵ�����
					row.setValue(bodyData.nameToIndex("defitem44"),amountAfter);
					// ��ҳ����Ա���Ԥ�ƿ�˰
					row.setValue(bodyData.nameToIndex("defitem46"),tax);
				} else if (incomeType.contains("���")) {
					amountAfter = ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(amountBefore);
					// ����Ԥ�ƿ�˰
					Double tax = amountBefore - amountAfter;
					// ��ҳ�����ʵ�����
					row.setValue(bodyData.nameToIndex("defitem44"),amountAfter);
					// ��ҳ����Ա���Ԥ�ƿ�˰
					row.setValue(bodyData.nameToIndex("defitem46"),tax);
				}else{
					AppInteractionUtil.showMessageDialog("��֧��Ŀ��Ϊ����ѻ��߸�ѣ��޷��������㹫ʽ !", "����", "ȷ��");
				}
			}else{
				AppInteractionUtil.showMessageDialog("��֧��ĿΪ�գ�����ѡ����֧��Ŀ!", "����", "ȷ��");
			}
			
			
			
		}
	}
	
	
	/**
	 * �õ�ʵ�������������Ԥ�ƿ�˰ �� Ӧ�����
	 * 
	 * Ӧ����� ��amount ����Ԥ�ƿ�˰ �� defitem46 ʵ����� : defitem44
	 */
	private void setValue_02(Dataset bodyData,Row row) {
		// ��ȡ��֧��Ŀ��ֵ
		String pk_incomeType = (String) row.getValue(bodyData.nameToIndex("szxmid"));
		// ��֧��Ŀ����ת��
		String incomeType = HRATCommonUtils.queryInoutbusiclassByName(pk_incomeType);
		// �õ� defitem44 ʵ�����
		Double amountAfter = Double.parseDouble(row.getValue(bodyData.nameToIndex("defitem44")).toString());
		// �ж�ʵ������Ƿ����
		if (amountAfter < 0) {
			// ������븺ֵ���� ,������ʾ��Ϣ,���������Ӧ��������
			AppInteractionUtil.showMessageDialog("��������ȷ���,ʵ������Ϊ��ֵ!", "����", "ȷ��");
			row.setValue(bodyData.nameToIndex("amount"),new UFDouble(0));
			row.setValue(bodyData.nameToIndex("defitem44"),new UFDouble(0));
			row.setValue(bodyData.nameToIndex("defitem46"),"");
		} else if(amountAfter == 0){
			// nothing TODO
		} else {
			if(HRATCommonUtils.isNotEmpty(incomeType)){
				// ������֧��Ŀ������������Ӧ�����
				Double amountBefore = 0D;
				if (incomeType.contains("�����")) {
					amountBefore = ServiceFeeTaxUtils.serviceFeeCaculatePreTaxByAfterTax(amountAfter);
					// ����Ԥ�ƿ�˰
					Double tax = amountBefore - amountAfter;
					// ��ҳ�����Ӧ�����
					row.setValue(bodyData.nameToIndex("amount"),amountBefore);
					// ��ҳ����Ա���Ԥ�ƿ�˰
					row.setValue(bodyData.nameToIndex("defitem46"),tax);
				} else if (incomeType.contains("���")) {
					amountBefore = ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(amountAfter);
					// ����Ԥ�ƿ�˰
					Double tax = amountBefore - amountAfter;
					// ��ҳ�����Ӧ�����
					row.setValue(bodyData.nameToIndex("amount"),amountBefore);
					// ��ҳ����Ա���Ԥ�ƿ�˰
					row.setValue(bodyData.nameToIndex("defitem46"),tax);
				}else{
					AppInteractionUtil.showMessageDialog("��ȷ����֧��Ŀ����Ϊ��������ͻ��߸������,����ִ�м����߼� ��", "����", "ȷ��");
				}
			}
		}
	}
	
}
