package com.example.nexum_trabajador.domain.use_cases.contract

import javax.inject.Inject

/**
 * @author Santiago Varela Daza
 * @email svarela03@uan.edu.co
 * @github https://github.com/sanvarela03
 * @version 1.0
 */
data class ContractUseCases @Inject constructor(
    val getWorkerContracts: GetWorkerContractsUseCase,
    val getContractById: GetContractByIdUseCase,
    val verifyMeeting: VerifyMeetingUseCase,
    val generateInvoice: GenerateInvoiceUseCase,
    val completeWorkByWorker: CompleteWorkByWorkerUseCase,
    val scheduleMeeting: ScheduleMeetingUseCase
)
