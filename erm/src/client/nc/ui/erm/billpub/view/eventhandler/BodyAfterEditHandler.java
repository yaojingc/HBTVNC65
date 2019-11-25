package nc.ui.erm.billpub.view.eventhandler;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.erm.billpub.action.HRATCommonUtils;
import nc.ui.erm.billpub.action.IdCardUtil;
import nc.ui.erm.billpub.action.ServiceFeeTaxUtils;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.util.mmf.framework.base.MMValueCheck;
import nc.ui.pub.beans.MessageDialog;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.SqlBuilder;
import java.util.Objects;

public class BodyAfterEditHandler {

	private ErmBillBillForm editor = null;

	// ����һ�������������ѱ������Ľ�������
	private static final String TRANSTYPE = "264X-Cxx-01";

	public BodyAfterEditHandler(ErmBillBillForm editor) {
		this.editor = editor;
	}

	/**
	 * 
	 * @Title: doEdit
	 * @Description: ���崦��༭�¼���ֵ�ķ���
	 * @param @param editField �����������ڱ༭���ֶ���
	 * @return void ��������
	 * @throws
	 */
	public void doEdit(String editField, int row) {
		// ��ȡ��������
		String selectBillTypeCode = ((ErmBillBillManageModel) editor.getModel()).getSelectBillTypeCode();
		// �жϽ��������Ƿ��������
		if (TRANSTYPE.equals(selectBillTypeCode)) {
			switch (editField) {
			case "defitem50":
				// У��1����֤��������ͣ����л����������ʱ��������ֵ
				this.check_01(editField, row);

				break;

			case "defitem37":
				// У��2�� ��֤�ֻ�����
				this.check_02(editField, row);

				break;

			case "defitem42":
				// У��2_1�������Ϣ
				this.check_02(editField, row);
				// У��7��UAP��Ա����״̬Ϊ���á���֯Ϊ̨��������Ա���Ϊxxʱ�����ݵ����е����֤����ʶ�𣬲��������ѱ�����У�顣
				this.check_07(editField, row);
				break;

			case "defitem36":
				// У��3����֤�Ƿ����ֳ����� �ֶ�
				this.check_03(editField, row);

				break;

			case "defitem48":
				// У��4����֤���������֤�����һ����д������ʱҪ��ƥ��
				this.check_04(editField, row);

				break;
			case "defitem43":
				// У��5���л�֤��������Ӧ����������
				this.check_05(editField, row);

				break;

			case "szxmid":
				// У��6���л���֧��Ŀ�� ��� �����ֵ
				this.check_06(editField, row);

				break;

			case "amount":
				// ����1 : ����Ӧ�������� ����Ԥ�ƿ�˰ ��ʵ�����
				this.setValue_01(editField, row);

				break;

			case "defitem44":
				// ����2 : ����ʵ�������� ����Ԥ�ƿ�˰ ��Ӧ�����
				this.setValue_02(editField, row);

				break;

			default:
				break;
			}

			/**
			 * something TODO
			 */
		}

	}

	/**
	 * UAP��Ա����״̬Ϊ���á���֯Ϊ̨���������ݵ����е����֤����ʶ�𣬲��������ѱ�����У�顣��Ա���Ϊ�����ݡ���ְ���Ա�������ѣ�
	 * 
	 */
	private void check_07(String editField, int row) {
		// �õ�ǰ̨��д�����֤����
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");
		// �õ�ǰ̨��д������
		String name = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem48");

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
					MessageDialog.showErrorDlg(null, "����:", "��" + (row + 1) + " �е���Ա����ְ��Ա,������������ѣ���˶Ժ���д��");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem48");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem42");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
					editor.getBillCardPanel().setBodyValueAt(null, row, "defitem39");
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 
	 * �õ�Ӧ�������������Ԥ�ƿ�˰ �� ʵ�����
	 * 
	 * Ӧ����� ��amount ����Ԥ�ƿ�˰ �� defitem46 ʵ����� : defitem44 ��֧��Ŀ:szxmid
	 */
	private void setValue_01(String editField, int row) {
		// ��ȡ��֧��Ŀ��ֵ
		String incomeType = editor.getBillCardPanel().getBodyValueAt(row, "szxmid").toString();
		// �õ� amount Ӧ�����
		Double amountBefore = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(row, "amount").toString());
		if (amountBefore < 0) {
			// ������븺ֵ���� ,������ʾ��Ϣ,���������Ӧ��������
			MessageDialog.showErrorDlg(null, "����", "��������ȷ���,Ӧ������Ϊ��ֵ!");
			editor.getBillCardPanel().setBodyValueAt(null, row, "amount");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem44");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem46");
		} else {
			// ������֧��Ŀ������������ʵ�����
			Double amountAfter = 0D;
			if (incomeType != null && incomeType.contains("�����")) {
				amountAfter = ServiceFeeTaxUtils.serviceFeeCaculateAfterTaxByPreTax(amountBefore);
			} else if (incomeType != null && incomeType.contains("���")) {
				amountAfter = ServiceFeeTaxUtils.airticleFeeCaculateAfterTaxByPreTax(amountBefore);
			}
			// ����Ԥ�ƿ�˰
			Double tax = amountBefore - amountAfter;
			// ��ҳ�����ʵ�����
			editor.getBillCardPanel().setBodyValueAt(amountAfter, row, "defitem44");
			// ��ҳ����Ա���Ԥ�ƿ�˰
			editor.getBillCardPanel().setBodyValueAt(tax, row, "defitem46");
		}

	}

	/**
	 * �õ�ʵ�������������Ԥ�ƿ�˰ �� Ӧ�����
	 * 
	 * Ӧ����� ��amount ����Ԥ�ƿ�˰ �� defitem46 ʵ����� : defitem44
	 */
	private void setValue_02(String editField, int row) {
		// ��ȡ��֧��Ŀ��ֵ
		String incomeType = editor.getBillCardPanel().getBodyValueAt(row, "szxmid").toString();
		// �õ� defitem44 ʵ�����
		Double amountAfter = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(row, "defitem44").toString());
		// �ж�ʵ������Ƿ����
		if (amountAfter < 0) {
			// ������븺ֵ���� ,������ʾ��Ϣ,���������Ӧ��������
			MessageDialog.showErrorDlg(null, "����", "��������ȷ���,ʵ������Ϊ��ֵ!");
			editor.getBillCardPanel().setBodyValueAt(null, row, "amount");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem44");
			editor.getBillCardPanel().setBodyValueAt(null, row, "defitem46");
		} else {
			// ������֧��Ŀ������������Ӧ�����
			Double amountBefore = 0D;
			if (incomeType != null && incomeType.contains("�����")) {
				amountBefore = ServiceFeeTaxUtils.serviceFeeCaculatePreTaxByAfterTax(amountAfter);
			} else if (incomeType != null && incomeType.contains("���")) {
				amountBefore = ServiceFeeTaxUtils.airticleFeeCaculatePreTaxByAfterTax(amountAfter);
			}
			// ����Ԥ�ƿ�˰
			Double tax = amountBefore - amountAfter;
			// ��ҳ�����Ӧ�����
			editor.getBillCardPanel().setBodyValueAt(amountBefore, row, "amount");
			// ��ҳ����Ա���Ԥ�ƿ�˰
			editor.getBillCardPanel().setBodyValueAt(tax, row, "defitem46");
			// �����ԭ�ҽ��
			editor.getBillCardPanel().setBodyValueAt(amountBefore, row, "ybje");
		}

		int i = editor.getBillCardPanel().getRowCount();
		// �õ� amount Ӧ�����
		Double amount = 0D;
		Double headAmount = 0D;
		for (int j = 0; j < i; j++) {
			amount = Double.parseDouble(editor.getBillCardPanel().getBodyValueAt(j, "amount").toString());
			headAmount = headAmount + amount;
		}
		editor.getBillCardPanel().setHeadItem("total", headAmount);
		editor.getBillCardPanel().setHeadItem("bbje", headAmount);
		editor.getBillCardPanel().setHeadItem("ybje", headAmount);
	}

	/**
	 * 1.��Ϊ˰ǰ����ʱ��ֻ�ܱ༭Ӧ����� 2.��Ϊ˰������ʱ��ֻ�ܱ༭ʵ�����
	 * 
	 * defitem50 ��������� amount Ӧ����� defitem44 ʵ�����
	 */
	private void check_01(String editField, int row) {
		// �õ� defitem50 ���������
		String defitem50 = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem50");

		BillItem amountyingfa = editor.getBillCardPanel().getBodyItem("amount");

		BillItem amountshifa = editor.getBillCardPanel().getBodyItem("defitem44");

		if (MMValueCheck.isNotEmpty(defitem50)) {
			// ����ǰ�б�������������Ϊ˰ǰ����
			if ("˰ǰ����".equals(defitem50)) {
				amountyingfa.setEnabled(true);// �����ÿɱ༭���Ƿ�ɱ༭Ҫ�����������
				amountshifa.setEnabled(false);
			} else {
				amountyingfa.setEnabled(false);// �����ÿɱ༭���Ƿ�ɱ༭Ҫ�����������
				amountshifa.setEnabled(true);
			}
		}

		// Ӧ�����
		editor.getBillCardPanel().setBodyValueAt("", row, "amount");
		// ʵ�����
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem44");
		// ����Ԥ�ƿ�˰��defitem46
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem46");
		// ������˰��defitem45
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem45");
		// �ۼ�Ӧ�� defitem47
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem47");
	}

	/**
	 * ��֤�ֻ����룬�����Ϣ
	 * 
	 * �ֻ����� defitem37 ֤������ defitem42
	 * 
	 * 2.��д���������֤��֮���Զ����� �Ա𣬳������ڣ���������
	 * 
	 * ֤������ defitem43 ֤������ defitem42 �������� defitem41 �Ա� defitem40 �������� defitem39
	 * �������� �������� defitem38
	 */
	private void check_02(String editField, int row) {
		// �õ�ǰ̨��д���ֻ�����
		String phoneNumber = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem37");
		// �õ�ǰ̨��д��֤������
		String idCardType = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem43");
		// �õ�ǰ̨��д�����֤����
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");

		// ����ǰ�༭���ֶ�Ϊ defitem37 �ֻ�����
		if ("defitem37".equals(editField)) {
			if (MMValueCheck.isNotEmpty(phoneNumber)) {
				if (phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
					MessageDialog.showErrorDlg(null, "����", "��ǰ�У��ֻ�������д���󣬳���ӦΪ11λ������1��ͷ��");
					// ��֮ǰ��д������ֻ�������յ�
					editor.getBillCardPanel().setBodyValueAt("", row, "defitem37");
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
						MessageDialog.showErrorDlg(null, "����", "��ǰ�У����֤�� �� " + idCardNo + "   ���Ϸ�,��˶� ��");
						// ��֮ǰ��д��������֤����յ�
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem42");
					} else {
						// У�����֤��ͨ��֮�󣬸����漸���ֶθ�ֵ
						// ����������ֵ����֤���������֤ʱ��Ĭ��Ϊ�й�
						editor.getBillCardPanel().setBodyValueAt(HRATCommonUtils.queryDefdocPk("�й�", "����"), row, "defitem41");
						// ���Ա�ֵ
						editor.getBillCardPanel().setBodyValueAt(IdCardUtil.getSexFromidCardNo(idCardNo), row, "defitem40");
						// ���������ڸ�ֵ
						editor.getBillCardPanel().setBodyValueAt(IdCardUtil.getBirthDayFromidCardNo(idCardNo), row, "defitem39");
						// ���������� ����������ֵ
						editor.getBillCardPanel().setBodyValueAt(HRATCommonUtils.queryDefdocPk("�й�", "����"), row, "defitem38");
					}
				} else {
					// ��֤�����Ͳ������֤��ʱ�������Ӧ����ͨ�������������� idCardType��֤������ idCardNo����У�飬
					boolean flag = HRATCommonUtils.judgeIDNumberIsLegal(idCardType, idCardNo);
					if (flag) {
						MessageDialog.showErrorDlg(null, "����", "��ǰ�У�֤������ �� " + idCardNo + "   ���Ϸ�,��˶� ��");
						// ��֮ǰ��д��������֤����յ�
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem42");
					}
				}
			} else {
				// ��������ֵΪ��
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
				// ���Ա�ֵΪ��
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
				// ���������ڸ�ֵΪ��
				editor.getBillCardPanel().setBodyValueAt("", row, "defitem39");
				// ���������� ����������ֵΪ��
				editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
			}
		}
	}

	/**
	 * �Ƿ����ֳ����ţ����գ�ѡ��Y��N�����ѡ��N����������𡢿������С������˻���Ҫ��� �Ƿ����ֳ����� defitem36 �������
	 * defitem35 �������� defitem34 ���п��� defitem33
	 */
	private void check_03(String editField, int row) {
		// �õ�ǰ̨ѡ��� �Ƿ��ֳ����� �ֶ�ֵ
		String isSitetoExtend = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem36");

		BillItem bankType = editor.getBillCardPanel().getBodyItem("defitem35");
		BillItem bankName = editor.getBillCardPanel().getBodyItem("defitem34");
		BillItem bankNo = editor.getBillCardPanel().getBodyItem("defitem33");

		if ("��".equals(isSitetoExtend)) {
			bankType.setNull(true);
			bankName.setNull(true);
			bankNo.setNull(true);
		} else {
			bankType.setNull(false);
			bankName.setNull(false);
			bankNo.setNull(false);
		}
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
	private void check_04(String editField, int row) {
		// �õ�ǰ̨��д�����֤����
		String idCardNo = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem42");
		// �õ�ǰ̨��д������
		String name = (String) editor.getBillCardPanel().getBodyValueAt(row, "defitem48");

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
			sql.append("tb_h.djlxbm", this.TRANSTYPE);
			sql.append(" 	   AND ");
			sql.append(" tb_b.defitem42 ", idCardNo);
			sql.append("  ORDER BY tb_b.ts ASC ");

			try {
				Object result = queryBS.executeQuery(sql.toString(), new ColumnProcessor());

				if (HRATCommonUtils.isNotEmpty(result)) {
					String nameOril = result.toString();
					if (!nameOril.equals(name)) {
						MessageDialog.showErrorDlg(null, "����", "��ǰ��,���� �� " + name + "  ��ԭʼ���������ƥ�䣬��˶Ժ���д��");
						// ��֮ǰ��д��������֤����յ�
						editor.getBillCardPanel().setBodyValueAt("", row, "defitem48");
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
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
	private void check_05(String editField, int row) {
		String idType = editor.getBillCardPanel().getBodyValueAt(row, "defitem43").toString();
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem42");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem41");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem40");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem39");
		editor.getBillCardPanel().setBodyValueAt(null, row, "defitem38");
	}

	/**
	 * ѡ����֧��Ŀ֮����ܱ༭��Ӧ�Ľ�������Ӧ�Ĺ�ʽ
	 * 
	 * @param editField
	 * @param row
	 *            ʵ����defitem44 Ӧ����amount ����Ԥ�ƿ�˰��defitem46 ������˰��defitem45
	 *            �ۼ�Ӧ�� defitem47
	 */
	private void check_06(String editField, int row) {
		// Ӧ�����
		editor.getBillCardPanel().setBodyValueAt("", row, "amount");
		// ʵ�����
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem44");
		// ����Ԥ�ƿ�˰��defitem46
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem46");
		// ������˰��defitem45
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem45");
		// �ۼ�Ӧ�� defitem47
		editor.getBillCardPanel().setBodyValueAt("", row, "defitem47");
	}

	protected void setHeadValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null)
			getBillCardPanel().getHeadItem(key).setValue(value);
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}

}
