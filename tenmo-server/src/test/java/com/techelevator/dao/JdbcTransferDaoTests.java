package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.exception.TransferNotFound;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test(expected = TransferNotFound.class)
    public void get_transfer_by_invalid_id_throws_exception(){
        sut.getTransferById(4000);
    }

    @Test
    public void get_transfer_by_id_get_transfer(){
        sut.getTransferById(3001);
    }

    @Test
    public void transfer_history_returns_list(){
        List<Transfer> allTransfers = new ArrayList<>();
        allTransfers = sut.transferHistory(2001);

        Assert.assertNotNull(allTransfers);
        Assert.assertEquals(3, allTransfers.size());
    }


}
