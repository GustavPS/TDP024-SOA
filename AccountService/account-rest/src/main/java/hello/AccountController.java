package hello;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;

import java.util.List;
import com.google.gson.Gson;



@RestController
@RequestMapping("account-rest")
public class AccountController {
    // --- Here we choose the implementations of the logic and data layer --- //
    private TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    private final AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB(this.transactionEntityFacade),
                                                                                    this.transactionEntityFacade,
                                                                                    new HTTPHelperImpl());
    //----------------------------------------------------------------------- //

    @RequestMapping(path="/account/create")
    public ResponseEntity create(@RequestParam(value = "accounttype", required=false) String accounttype,
                                 @RequestParam(value = "person", required = false) String person,
                                 @RequestParam(value="bank", required=false) String bank) {
        Long personID;
        String json;

        if (accounttype == null || person == null || bank == null) {
            json = "FAILED";
        } else {
            try {
                personID = Long.parseLong(person);
                boolean result = accountLogicFacade.create(accounttype, personID, bank);
                if (result) {
                    json = "OK";
                } else {
                    json = "FAILED";
                }
            } catch(Exception e) {
                json = "FAILED";
            }
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(path="/account/find/person", produces="application/json")
    public ResponseEntity findPerson(@RequestParam long person) {
        List<Account> accountList = accountLogicFacade.find(person);
        String json = new Gson().toJson(accountList);
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(path="/account/debit", produces="application/json")
    public ResponseEntity debitAccount(@RequestParam long id, @RequestParam int amount) {
        boolean result = accountLogicFacade.debit(id, amount);
        String json;
        if (result) {
            json = new Gson().toJson("OK");
        } else {
            json = new Gson().toJson("FAILED");
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(path="/account/credit", produces="application/json")
    public ResponseEntity creditAccount(@RequestParam long id, @RequestParam int amount) {
        boolean result = accountLogicFacade.credit(id, amount);
        String json;
        if (result) {
            json = new Gson().toJson("OK");
        } else {
            json = new Gson().toJson("FAILED");
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(path="/account/transactions", produces="application/json")
    public ResponseEntity listTransactions(@RequestParam long id) {
        List<Transaction> transactionList = accountLogicFacade.getTransactions(id);
        String json = new Gson().toJson(transactionList);
        return new ResponseEntity(json, HttpStatus.OK);
    }


    /*

    @RequestMapping(path="/find/{key}", produces="application/json")
    public ResponseEntity list(@PathVariable("key") int key) {

      String json = new Gson().toJson("hello world this is find " + key);
      return new ResponseEntity(json,HttpStatus.OK);
    }

    @RequestMapping(path="/find", produces = "application/json", params={"name"})
    public ResponseEntity find(@RequestParam String name) {
      List<Person> personList = personLogicFacade.find(name);
      String json = new Gson().toJson(personList);
      return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(path="/find", produces = "application/json", params={"key"})
    public ResponseEntity find(@RequestParam long key) {
      Person person = personLogicFacade.find(key);
      String json = new Gson().toJson(person);
      return new ResponseEntity(json, HttpStatus.OK);
    }
    */



}
