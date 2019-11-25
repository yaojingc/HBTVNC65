package nc.individuation.defaultData;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JViewport;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.core.util.ObjectCreator;
import nc.bs.logging.Logger;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.funcnode.ui.statusbar.FuncletStatusBarCompCenter;
import nc.individuation.property.pub.AbstractIndividuationPage;
import nc.individuation.property.pub.IExceptionHandler;
import nc.individuation.property.pub.IIndividuationPage;
import nc.individuation.property.pub.IndividualSetting;
import nc.individuation.property.pub.IndividuationManager;
import nc.itf.uap.rbac.IUserManage;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.newinstall.util.StringUtil;
import nc.pubitf.setting.defaultdata.IUAPDefaultSettingConst;
import nc.ui.funcode.statusbar.DefaultOrgInfoStatusComp;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.sm.UserVO;
import nc.vo.util.ItfExtendManager;

public class DefaultConfigPage extends AbstractIndividuationPage
  implements IUAPDefaultSettingConst
{
  private static final long serialVersionUID = 4687770116569608608L;
  private IndividualSetting indivSettings = null;

  private BusinessOrgConfigPanel orgPanel = null;

  private FormatConfigPanel formatPanel = null;

  private IUserManage umService = null;

  private UserVO user = null;

  private IUserManageQuery umQryService = null;

  public DefaultConfigPage() {
    initUI();
  }

  private void initUI() {
    setLayout(new BoxLayout(this, 1));

    String implClassNameByItf = ItfExtendManager.getInstance().getImplClassNameByItf(IOrgConfigPanel.class.getName());
    if (!StringUtil.isEmpty(implClassNameByItf)) {
      String[] s = implClassNameByItf.split("-");
      this.orgPanel = ((BusinessOrgConfigPanel)ObjectCreator.newInstance(s[1], s[0]));
    }

    this.orgPanel.setOpaque(false);
    this.orgPanel.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance().getStrByID("sfapp", "DefaultConfigPage-000000")));

    this.formatPanel = new FormatConfigPanel();
    this.formatPanel.setOpaque(false);
    this.formatPanel.setBorder(BorderFactory.createTitledBorder(NCLangRes.getInstance().getStrByID("sfapp", "DefaultConfigPage-000001")));

    GridBagLayout gl = new GridBagLayout();
    UIPanel mainPane = new UIPanel(gl);
    mainPane.setOpaque(false);

    GridBagConstraints gbconstraint = new GridBagConstraints();
    gbconstraint.fill = 0;
    gbconstraint.weighty = 0.0D;
    gbconstraint.weightx = 0.0D;
    gbconstraint.gridx = 0;
    gbconstraint.gridy = 0;
    gbconstraint.insets = new Insets(5, 5, 5, 5);
    gbconstraint.anchor = 18;
    gl.setConstraints(this.orgPanel, gbconstraint);
    mainPane.add(this.orgPanel);

    gbconstraint.gridx = 0;
    gbconstraint.gridy = 1;
    gbconstraint.anchor = 18;
    gl.setConstraints(this.formatPanel, gbconstraint);
    mainPane.add(this.formatPanel);

    gbconstraint.gridx = 0;
    gbconstraint.gridy = 2;
    gbconstraint.weightx = 1.0D;
    gbconstraint.weighty = 1.0D;
    gbconstraint.anchor = 18;
    UIPanel spacePane = new UIPanel();
    gl.setConstraints(spacePane, gbconstraint);
    mainPane.add(spacePane);

    UIScrollPane scrlPane = new UIScrollPane();
    scrlPane.setViewportView(mainPane);
    scrlPane.getViewport().setOpaque(false);
    add(scrlPane);
  }

  public void initData(IIndividuationPage icp)
  {
    try
    {
      this.indivSettings = IndividuationManager.getIndividualSetting(getClass(), false);

      this.user = getUserQryService().getUser(WorkbenchEnvironment.getInstance().getLoginUser().getPrimaryKey());
    } catch (Exception e) {
      if (getExceptionHandler() != null)
        getExceptionHandler().handleException(e);
      else {
        Logger.error(e.getMessage(), e);
      }

    }

    settingsToView();
  }

  private void settingsToView()
  {
    this.orgPanel.settingsToView(this.indivSettings);

    this.formatPanel.setFormat(this.user.getFormat());
    this.formatPanel.setLangtype(this.user.getContentlang());
  }

  private void viewToSettings()
  {
    this.orgPanel.viewToSettings(this.indivSettings);

    String lanytype = this.formatPanel.getLangtype();
    this.user.setContentlang(lanytype);

    String formatid = this.formatPanel.getFormat();
    this.user.setFormat(formatid);
  }

  public void onApply()
    throws Exception
  {
    viewToSettings();

    this.user = getUserServie().simpleUpdateUser(this.user);

    setIndividualSettings(new IndividualSetting[] { this.indivSettings });
    super.onApply();

    FuncletStatusBarCompCenter.getFuncletStatusBarCompCenter().fireComponentUpdateEvent(DefaultOrgInfoStatusComp.class);
  }

  public String getDescription()
  {
    return NCLangRes.getInstance().getStrByID("sfapp", "DefaultConfigPage-000002");
  }

  public String getTitle()
  {
    return NCLangRes.getInstance().getStrByID("sfapp", "DefaultConfigPage-000003");
  }

  private IUserManage getUserServie() {
    if (this.umService == null) {
      this.umService = ((IUserManage)NCLocator.getInstance().lookup(IUserManage.class));
    }
    return this.umService;
  }

  private IUserManageQuery getUserQryService() {
    if (this.umQryService == null) {
      this.umQryService = ((IUserManageQuery)NCLocator.getInstance().lookup(IUserManageQuery.class));
    }
    return this.umQryService;
  }
}