package eum.backed.server.controller.bank;

import eum.backed.server.common.DTO.DataResponse;
import eum.backed.server.controller.bank.dto.request.BankAccountRequestDTO;
import eum.backed.server.controller.bank.dto.response.BankAccountResponseDTO;
import eum.backed.server.domain.bank.bankacounttransaction.TrasnactionType;
import eum.backed.server.service.bank.BankAccountService;
import eum.backed.server.service.bank.BankTransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-account")
@RequiredArgsConstructor
@Api(tags = "bank account ")
@Slf4j
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankTransactionService bankTransactionService;

    @PutMapping("/password")
    @ApiOperation(value = "카드 비밀 번호 바꾸기")
    public DataResponse updatePassword(@RequestBody BankAccountRequestDTO.UpdatePassword updatePassword,@AuthenticationPrincipal String email){
        return bankAccountService.updatePassword(updatePassword,email);
    }
    @PostMapping("/remittance")
    @ApiOperation(value = "거래(송금하기)")
    public DataResponse remittance(@RequestBody BankAccountRequestDTO.Remittance remittance, @AuthenticationPrincipal String email){
        return bankAccountService.remittance(remittance, email);
    }
    @GetMapping("/{transactionType}")
    @ApiOperation(value = "거래 내역 조회",notes = "transactionType 별 전체 입출금 필터")
    public DataResponse<List<BankAccountResponseDTO.GetAllHistory>> getAllHistory(@PathVariable TrasnactionType transactionType,@AuthenticationPrincipal String email){
        return bankTransactionService.getAllHistory(email,transactionType);
    }
}
