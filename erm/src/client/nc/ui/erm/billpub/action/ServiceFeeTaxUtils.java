package nc.ui.erm.billpub.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nc.bs.er.exp.cmd.ServiceFeeCalcVO;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.pub.SqlBuilder;

/**
 * 劳务费收入扣税计算工具类
 * 
 * 劳务报酬所得，每次收入不超过四千元的，减除费用八百元；四千元以上的，减除百分之二十的费用，其余
 * 额为应纳税所得额。然后适用比例税率，税率为百分之二十。
 * 
 *
 * |收入(x)         |应纳税部分(y)   |      阶梯    		  |  税率               | 纳税额                             | 速扣系数               |
 * -----------------------------------------------------------------------------------------------
 * |x<=800         |      0       |     0             |  0        | 0               | 0           |
 * |800<x<=4000    |    x-800     |    y<=20000       |  20%      | (x-800)*20%     | 0           |
 * |4000<x<=20000  |    0.8*x     |    y<=20000       |  20%      | 0.8x*20%        | 0           |
 * |20000<x<=50000 |    0.8*x     |    20000<y<=50000 |  30%      | 0.8x*30%        | 2000        |
 * |x>50000        |    0.8*x     |    y>50000        |  40%      | 0.8x*40%        | 7000        |
 *
 *
 *  应纳税所得额 = 稿酬所得（不超过4000元） - 800元
	应纳税所得额 = 稿酬所得（超过4000元）×（1 - 20%）
	应纳税额 = 应纳税所得额 ×14%
 *
 *
 * @author yao
 */
public class ServiceFeeTaxUtils {
	
	/**
     * 定义一个常量配置小数点后精确位的位数，小数点后保留位数，2位，四舍五入，后期可以更改配置
     */
    private static final int DIGIT_NUMBER_AFTER_DOT = 2;
	
    // 定义一个常量存放劳务费报销单的交易类型
 	public static final String TRANSTYPE = "264X-Cxx-01";
	
    /**
     * 根据输入的税前劳务费，计算得到税后劳务费
     * @param originServiceFee
     * @return
     */
	public static Double serviceFeeCaculateAfterTaxByPreTax(double originServiceFee){
		//应纳税部分
        double taxIncome = 0;
        double taxRate = 0;
        double quickDeducData = 0;
        if (originServiceFee <= 800) {
            return originServiceFee;
        } else if (originServiceFee > 800 && originServiceFee <= 4000) {
            taxIncome = originServiceFee - 800;
            taxRate = 0.2;
        }
        if (originServiceFee > 4000) {
            taxIncome = originServiceFee * 0.8;
        }
        if (taxIncome <= 20000) {
            taxRate = 0.2;
        } else if (taxIncome > 20000 && taxIncome <= 50000) {
            taxRate = 0.3;
            quickDeducData = 2000;
        } if (taxIncome > 50000) {
            taxRate = 0.4;
            quickDeducData = 7000;
        }
        double incomeAfterTax = originServiceFee - (taxIncome * taxRate - quickDeducData);
        return roundHalfUpValue(incomeAfterTax, DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	/**
	 * 根据输入的税后劳务费，计算得到税前劳务费
	 * @param afterTaxServiceFee
	 * @return
	 */
	public static Double serviceFeeCaculatePreTaxByAfterTax(double afterTaxServiceFee){
		//应纳税部分
        double taxIncome = 0;
        double tax = 0;
        
        if (afterTaxServiceFee <= 800) {
            return afterTaxServiceFee;
        } else if (afterTaxServiceFee > 800 && afterTaxServiceFee <= 3360) {
        	// 应纳税所得额=（不含税收入额-800）÷（1-税率）
            taxIncome = (afterTaxServiceFee - 800) / (1-0.2) + 800;
        } else if (afterTaxServiceFee > 3360 && afterTaxServiceFee <= 21000) {
        	// 应纳税所得额＝[（不含税收入额－速算扣除数）×（1－20%）]÷[1－税率×（1－20%）]
        	taxIncome = (afterTaxServiceFee * 0.8) / (1-0.2*(1-0.2)) ;
        	// 应纳税额=应纳税所得额×适用税率-速算扣除数 
        	tax = taxIncome * 0.2;
        	taxIncome = afterTaxServiceFee + tax;
        } else if (afterTaxServiceFee > 21000 && afterTaxServiceFee <= 49500) {
        	taxIncome = ((afterTaxServiceFee - 2000) * 0.8) / 0.76 ;
        	// 应纳税额=应纳税所得额×适用税率-速算扣除数 
        	tax = taxIncome * 0.3 - 2000;
        	taxIncome = afterTaxServiceFee + tax;
        } else if (afterTaxServiceFee > 49500) {
        	taxIncome = ((afterTaxServiceFee - 7000) * 0.8) / 0.68 ;
        	// 应纳税额=应纳税所得额×适用税率-速算扣除数 
        	tax = taxIncome * 0.4 - 7000;
        	taxIncome = afterTaxServiceFee + tax;
        }
        return roundHalfUpValue(taxIncome, DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	
	/**
	 * 根据税前的稿费计算税后的稿费
	 * @param gaoFee
	 * @return
	 */
	public static Double airticleFeeCaculateAfterTaxByPreTax(double airticleFee){
		// 应纳税部分
		double taxIncome = 0;
        
        if(airticleFee <= 800){
        	return airticleFee;
        }else if (airticleFee > 800 && airticleFee <= 4000) {
            taxIncome = airticleFee - 800;
        }
        
        if (airticleFee > 4000) {
            taxIncome = airticleFee * 0.8;
        }
        
        // 应纳税额 = 应纳税所得额 ×14%
        double incomeAfterTax = airticleFee - taxIncome * 0.14;
		
		return roundHalfUpValue(incomeAfterTax,DIGIT_NUMBER_AFTER_DOT);
	}
	
	
	/**
	 * 根据税后的稿费计算税前的稿费
	 * @param gaoFee
	 * @return
	 */
	public static Double airticleFeeCaculatePreTaxByAfterTax(double airticleFee){
		// 应纳税部分
		double taxIncome = 0;
        
        if(airticleFee <= 800){
        	return airticleFee;
        } else if (airticleFee > 800 && airticleFee <= 3552) {
        	
        	taxIncome = ( airticleFee - 800 ) / 0.86 +800;
        	
        } else if (airticleFee > 3552) {
        	
        	taxIncome = airticleFee / 0.888;
        	
        }
        
		return roundHalfUpValue(taxIncome,DIGIT_NUMBER_AFTER_DOT);
				 
	}
	
	
	
	/**
	 * 数据精度处理方法
	 * @param value
	 * @param digitNumberAfterDot
	 * @return
	 */
    private static double roundHalfUpValue(double value, int digitNumberAfterDot) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.setScale(digitNumberAfterDot, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    
    
    /**
     * 通过身份证号，当月的日期查询该人的历史劳务费报销单，并计算得到本次需要的 累计应发 和 增补扣税的值
     * @param idcardno 身份证号
     * @param currentdate 当月的日期  yyyy-mm
     * @return  Map<身份证号,存放累计应发和增补扣税的VO> 
     */
    public static Map<String,ServiceFeeCalcVO>  getFeeMapByIDCardNO(String idcardno,String currentdate){
    	SqlBuilder sql = new SqlBuilder();
    	sql.append(" SELECT ");
    	sql.append(" 	tb_h.approver  as approver, ");// 表头的审批人
    	sql.append(" 	tb_b.defitem42 as idcardno, ");// 身份证号
    	sql.append(" 	tb_b.amount    as yfje, ");// 应发金额
    	sql.append(" 	tb_b.defitem47 as ljyf, ");// 累计应发
    	sql.append(" 	tb_b.defitem46 as bcyjks, ");// 本次预计扣税
    	sql.append(" 	tb_b.defitem45 as zbks, ");// 增补扣税
    	sql.append(" 	tb_b.defitem44 as sfje, ");// 实发金额
    	sql.append(" 	tb_b.ts	   	   as bodydate ");// 表体的日期
    	sql.append(" FROM ");
    	sql.append(" 	er_busitem tb_b ");
    	sql.append(" LEFT JOIN ");
    	sql.append(" 	er_bxzb tb_h ");
    	sql.append(" ON ");
    	sql.append(" 	tb_h.pk_jkbx = tb_b.pk_jkbx ");
    	sql.append(" WHERE ");
    	sql.append(" 	tb_h.djlxbm ", TRANSTYPE);
    	sql.append(" AND ");
    	sql.append(" 	tb_b.defitem42 ",idcardno);
    	sql.append(" AND ");
    	sql.append(" 	tb_b.ts like '"+currentdate+"%'");
    	sql.append(" AND ");
    	sql.append(" 	tb_h.Approver ");
    	sql.append(" IN (");
    	sql.append(" 		SELECT ");
    	sql.append(" 			wfgroup_b.pk_member ");
    	sql.append(" 		FROM ");
    	sql.append(" 			pub_wfgroup_b wfgroup_b ");
    	sql.append(" 		LEFT JOIN ");
    	sql.append(" 			pub_wfgroup wfgroup ");
    	sql.append(" 		ON ");
    	sql.append(" 			wfgroup.pk_wfgroup = wfgroup_b.pk_wfgroup ");
    	sql.append(" 		WHERE");
    	sql.append(" 			wfgroup.code = 'kj' ");
    	sql.append(" 		AND ");
    	sql.append(" 			wfgroup.name = '会计' ");
    	sql.append(" 	)");
    	
    	
    	IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
    	
    	// 初始化历史累计应发金额
    	UFDouble ljyf = new UFDouble();
    	// 初始化最终的历史本次预计扣税金额 和 历史增补扣税金额的和
    	UFDouble taxTotal = new UFDouble();
    	
    	try {
    		// 得到符合条件的历史劳务费报销单数据
			List<ServiceFeeCalcVO> list = (List<ServiceFeeCalcVO>) queryBS
					.executeQuery(sql.toString(), new BeanListProcessor(ServiceFeeCalcVO.class));    		
    		
    		if(HRATCommonUtils.isNotEmpty(list)){
    			for (int i = 0; i < list.size(); i++) {
					ServiceFeeCalcVO serviceFeeCalcVO  = (ServiceFeeCalcVO)list.get(i);
					
					UFDouble currLjyf = serviceFeeCalcVO.getLjyf() == null?new UFDouble(0):serviceFeeCalcVO.getLjyf();
					// 累加历史的累计应发
					ljyf = ljyf.add(currLjyf);
					
					UFDouble currBcyjks = serviceFeeCalcVO.getBcyjks() == null?new UFDouble(0):serviceFeeCalcVO.getBcyjks();
					UFDouble currZbks = serviceFeeCalcVO.getZbks() == null?new UFDouble(0):serviceFeeCalcVO.getZbks();
					// 累加历史本次预计扣税金额 和 历史增补扣税金额
					taxTotal = taxTotal.add(currBcyjks).add(currZbks);
    			}
    			
    			ServiceFeeCalcVO resultVO = new ServiceFeeCalcVO();
    			resultVO.setIdcardno(idcardno);
    			
    			if(ljyf.isTrimZero()){
    				resultVO.setLjyf(new UFDouble(0));
    			}else{
    				resultVO.setLjyf(ljyf);
    			}
    			
    			// 将最终历史的 本次预计扣税以及增补扣税全部累加到 一个全新VO的增补扣税字段上
    			resultVO.setZbks(taxTotal);
    			
    			Map<String,ServiceFeeCalcVO> map = new HashMap<String,ServiceFeeCalcVO>();
    			map.put(idcardno, resultVO);
    			
    			return map;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
	
	
}
