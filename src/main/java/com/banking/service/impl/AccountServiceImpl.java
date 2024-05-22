package com.banking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.dto.AccountDto;
import com.banking.mapper.AccountMapper;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import com.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService{

	
	private AccountRepository accountRepository;
	
	public AccountServiceImpl(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {

		Account account=AccountMapper.mapToAccount(accountDto);
		Account savedAccount=accountRepository.save(account);
		
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public AccountDto getAccountById(Long id) {

		Account account=accountRepository
				.findById(id)
				.orElseThrow(()-> new RuntimeException("Account does not exists"));
		
		return AccountMapper.mapToAccountDto(account);
	}

	@Override
	public AccountDto deposit(Long id, Double amount) {
		Account account=accountRepository
				.findById(id)
				.orElseThrow(()-> new RuntimeException("Account does not exists"));

		Double total=account.getBalance()+amount;
		account.setBalance(total);
		Account savedAccount=accountRepository.save(account);
		
		return AccountMapper.mapToAccountDto(savedAccount);
	}

	@Override
	public AccountDto withdraw(Long id, Double amount) {
		Account account=accountRepository
				.findById(id)
				.orElseThrow(()-> new RuntimeException("Account does not exists"));

		if(account.getBalance() < amount) {
			throw new RuntimeException("Insufficient amount");	
		}
		
		Double total=account.getBalance() - amount;
		account.setBalance(total);
		Account savAccount=accountRepository.save(account);
		
		return AccountMapper.mapToAccountDto(savAccount);
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		
		List<Account> accounts=accountRepository.findAll();
		
		return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
					.collect(Collectors.toList());
		
		
	}

	@Override
	public void deleteAccount(Long id) {
		Account account=accountRepository
				.findById(id)
				.orElseThrow(()-> new RuntimeException("Account does not exists"));

		accountRepository.deleteById(id);
	}

}
