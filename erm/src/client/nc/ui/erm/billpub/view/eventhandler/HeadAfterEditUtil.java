package nc.ui.erm.billpub.view.eventhandler;

import java.util.ArrayList;
import java.util.List;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.erm.billpub.model.ErmBillBillManageModel;
import nc.ui.erm.billpub.view.ErmBillBillForm;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.fipub.exception.ExceptionHandler;
import nc.vo.pub.bill.BillTabVO;
import nc.vo.uif2.LoginContext;

public class HeadAfterEditUtil {
	private ErmBillBillForm editor = null;

	public HeadAfterEditUtil(ErmBillBillForm editor) {
		this.editor = editor;
	}

	private BillCardPanel getBillCardPanel() {
		return this.editor.getBillCardPanel();
	}

	public String getHeadItemStrValue(String itemKey) {
		BillItem headItem = getBillCardPanel().getHeadItem(itemKey);
		return headItem == null ? null : (String) headItem.getValueObject();
	}

	public void initPayentityItems(boolean isEdit) {
		initItemsBelong(this.editor.getOrgRefFields("pk_org"),
				this.editor.getAllOrgRefFields(), "pk_org", null, isEdit);
	}

	public void initUseEntityItems(boolean isEdit) {
		initItemsBelong(this.editor.getOrgRefFields("dwbm"),
				this.editor.getAllOrgRefFields(), "dwbm", null, isEdit);
	}

	public void initCostentityItems(boolean isEdit) {
		initItemsBelong(this.editor.getOrgRefFields("fydwbm"),
				this.editor.getAllOrgRefFields(), "fydwbm", null, isEdit);
	}

	public void initPayorgentityItems(boolean isEdit) {
		initItemsBelong(this.editor.getOrgRefFields(JKBXHeaderVO.PK_PAYORG),
				this.editor.getAllOrgRefFields(), JKBXHeaderVO.PK_PAYORG, null,
				isEdit);
	}

	public void initItemsBelong(List<String> costentity_billitems,
			List<String> allitems, String key, Object fydwbm, boolean isEdit) {
		if (fydwbm == null) {
			fydwbm = getHeadValue(key);
		}
		String fyPkCorp = fydwbm == null ? null : fydwbm.toString();
		for (String item : costentity_billitems) {
			if (!item.equals(key)) {
				BillItem[] headItems = getItemsById(item);
				if (headItems != null) {
					for (BillItem headItem : headItems)
						if (headItem != null) {
							String refType = headItem.getRefType();
							if ((refType != null)
									&& (!refType.equals(""))
									&& (headItem.getComponent() != null)
									&& ((headItem.getComponent() instanceof UIRefPane)))
								try {
									UIRefPane ref = (UIRefPane) headItem
											.getComponent();

									boolean isInitGroup = false;
									isInitGroup = ((ErmBillBillManageModel) this.editor
											.getModel()).getContext()
											.getNodeCode().equals("20110CBSG");

									if (((isInitGroup) || ((fyPkCorp != null) && (!fyPkCorp
											.equals(""))))
											&& (headItem.isEnabled())) {
										if (!ref.isEnabled()) {
											ref.setEnabled(true);
										}
									}

									if (((fyPkCorp == null) || (fyPkCorp
											.equals("")))
											&& (!"zy".equals(headItem.getKey()))) {
										headItem.setValue(null);
									}

									AbstractRefModel model = ref.getRefModel();
									if (model != null) {
										model.setPk_org(fyPkCorp);
									}
									if (isEdit)
										if (headItem.getPos() == 0) {
											if (!"zy".equals(headItem.getKey()))
												headItem.setValue(null);
										} else if (headItem.getPos() == 1) {
											String tableCode = headItem
													.getTableCode();
											int rowCount = getBillCardPanel()
													.getBillModel(tableCode)
													.getRowCount();
											for (int i = 0; i < rowCount; i++)
												getBillCardPanel()
														.setBodyValueAt(
																null,
																i,
																headItem.getKey(),
																tableCode);
										}
								} catch (ClassCastException e) {
									ExceptionHandler.consume(e);
								}
						}
				}
			}
		}
		if (!isEdit) {
			String[] tables = getBillCardPanel().getBillData()
					.getBodyTableCodes();
			for (String tab : tables) {
				if ((tab == null) || (!tab.equals("er_cshare_detail"))) {
					BillItem[] bodyItems = getBillCardPanel().getBillData()
							.getBodyShowItems(tab);

					if (bodyItems != null) {
						List list = new ArrayList();
						for (BillItem bodyItem : bodyItems) {
							boolean flag = (costentity_billitems
									.contains(bodyItem.getKey()))
									|| ((bodyItem.getIDColName() != null) && (costentity_billitems
											.contains(bodyItem.getIDColName())));

							boolean fflag = (allitems.contains(bodyItem
									.getKey()))
									|| ((bodyItem.getIDColName() != null) && (allitems
											.contains(bodyItem.getIDColName())));

							if ((flag) || ((key.equals("pk_org")) && (!fflag))) {
								list.add(bodyItem);
							}

						}

						initAllitemsToCurrcorp(
								(BillItem[]) list.toArray(new BillItem[0]),
								fyPkCorp);
					}
				}
			}
		}
	}

	private void initAllitemsToCurrcorp(BillItem[] headItems, String pk_org) {
		boolean isInitGroup = false;
		isInitGroup = ((ErmBillBillManageModel) this.editor.getModel())
				.getContext().getNodeCode().equals("20110CBSG");

		for (BillItem headItem : headItems) {
			String refType = headItem.getRefType();
			if ((!headItem.getKey().equals("dwbm"))
					&& (!headItem.getKey().equals("fydwbm"))
					&& (!headItem.getKey().equals("pk_org"))) {
				if ((refType != null) && (!refType.equals(""))
						&& (headItem.getComponent() != null)
						&& ((headItem.getComponent() instanceof UIRefPane))) {
					try {
						UIRefPane ref = (UIRefPane) headItem.getComponent();
						AbstractRefModel refModel = ref.getRefModel();
						if (refModel != null) {
							if ((pk_org == null) && (!isInitGroup)) {
								ref.setEnabled(false);
								ref.setValue(null);
							} else if ((ref.getPk_corp() == null)
									|| (!ref.getPk_corp().equals(pk_org))) {
								ref.setPk_org(pk_org);
								ref.setValue(null);
								ref.setEnabled(true);
							}
						}
					} catch (ClassCastException e) {
						ExceptionHandler.consume(e);
					}

				}
			}
		}
	}

	protected BillItem[] getItemsById(String item) {
		if ((item.equals("szxmid")) || (item.equals("jobid"))
				|| (item.equals("cashproj")) || (item.equals("projecttask"))
				|| (item.equals("hbbm")) || (item.equals("customer"))
				|| (item.equals("freecust"))) {
			List results = new ArrayList();
			if (getBillCardPanel().getHeadItem(item) != null) {
				results.add(getBillCardPanel().getHeadItem(item));
			}

			BillTabVO[] billTabVOs = getBillCardPanel().getBillData()
					.getBillTabVOs(1);
			for (BillTabVO billTabVO : billTabVOs) {
				String metaDataPath = billTabVO.getMetadatapath();
				if ((metaDataPath == null)
						|| ("er_busitem".equals(metaDataPath))
						|| ("jk_busitem".equals(metaDataPath))) {
					String tab = billTabVO.getTabcode();
					BillItem[] bodyItems = getBillCardPanel().getBillData()
							.getBodyItemsForTable(tab);
					if (bodyItems != null) {
						for (BillItem key : bodyItems)
							if ((key.getKey().equals(item))
									|| ((key.getIDColName() != null) && (key
											.getIDColName().equals(item))))
								results.add(key);
					}
				}
			}
			return (BillItem[]) results.toArray(new BillItem[0]);
		}
		return new BillItem[] { getBillCardPanel().getHeadItem(item) };
	}

	protected Object getHeadValue(String key) {
		BillItem headItem = getBillCardPanel().getHeadItem(key);
		if (headItem == null) {
			headItem = getBillCardPanel().getTailItem(key);
		}
		if (headItem == null) {
			return null;
		}
		return headItem.getValueObject();
	}

	protected void setHeadValue(String key, Object value) {
		if (getBillCardPanel().getHeadItem(key) != null)
			getBillCardPanel().getHeadItem(key).setValue(value);
	}
}