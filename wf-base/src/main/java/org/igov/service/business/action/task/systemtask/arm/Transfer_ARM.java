package org.igov.service.business.action.task.systemtask.arm;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.igov.model.arm.DboTkModel;
import org.igov.model.arm.ValidationARM;
import org.igov.service.business.action.task.systemtask.mail.Abstract_MailTaskCustom;
import org.igov.service.business.arm.ArmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Elena Листинер, предназначен для создания новой заявки в АРМ
 *
 */
@Component("Transfer_ARM")
public class Transfer_ARM extends Abstract_MailTaskCustom implements JavaDelegate {

	private final static Logger LOG = LoggerFactory.getLogger(Transfer_ARM.class);

	private Expression soData;

	@Autowired
	private ArmService armService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// получаю из екзекьюшена sID_order
		String sID_order = generalConfig.getOrderId_ByProcess(Long.valueOf(execution.getProcessInstanceId()));
		LOG.info("sID_order in Transfer_ARM>>>>>>>>>>>" + sID_order);

		// получаю из екзекьюшена soData
		String soData_Value = this.soData.getExpressionText();
		LOG.info("soData_Value before: " + soData_Value);
		String soData_Value_Result = replaceTags(soData_Value, execution);
		LOG.info("soData_Value after: " + soData_Value_Result);

		// из мапы получаем по ключу значения и укладываем все это в
		// модель и туда же укладываем по ключу Out_number значение sID_order
		DboTkModel dataForTransferToArm = ValidationARM.fillModel(soData_Value_Result);

		Integer maxNum = armService.getMaxValue();
		LOG.info("int max.... " + maxNum);

		dataForTransferToArm.setNumber_441(maxNum + 1);
		LOG.info("int dataForTransferToArm.getNumber_441().... " + dataForTransferToArm.getNumber_441());

		Integer maxNum442 = armService.getMaxValue442();
		LOG.info("int max442.... " + maxNum442);
		dataForTransferToArm.setNumber_442(maxNum442+1);
		LOG.info("int dataForTransferToArm.getNumber_442().... " + dataForTransferToArm.getNumber_442());

		String prilog = ValidationARM.getPrilog(dataForTransferToArm.getPrilog(), oAttachmetService);
		LOG.info("prilog>>>>>>>>>>>> = {}", prilog);
		dataForTransferToArm.setPrilog(ValidationARM.isValidSizePrilog(prilog));
		LOG.info("dataForTransferToArm in TransferArm = {}", dataForTransferToArm);

		List<DboTkModel> listOfModels = armService.getDboTkByOutNumber(sID_order);
		LOG.info("listOfModels >>>>>>>>>>> from ARM ={}", listOfModels);
		if (listOfModels == null || listOfModels.isEmpty()) {
			armService.createDboTk(dataForTransferToArm);
		}

	}

}
