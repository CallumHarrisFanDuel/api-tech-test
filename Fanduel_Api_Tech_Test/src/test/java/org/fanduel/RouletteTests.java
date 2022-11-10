package org.fanduel;

import org.fanduel.models.GameRequest;
import org.fanduel.models.GameResponse;
import org.fanduel.models.Stake;
import org.fanduel.models.StakeType;
import org.fanduel.models.bet.HalfNumberBet;
import org.fanduel.models.bet.RedBlackBet;
import org.fanduel.models.bet.SingleBet;
import org.fanduel.models.bet.SquareBet;
import org.fanduel.services.DefaultRouletteEngine;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RouletteTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private DefaultRouletteEngine engine;

    @Test
    public void test_roulettePlaySingleBet() throws Exception {

        GameRequest gameRequest = new GameRequest();

        Stake stake = new Stake();
        stake.stakeType = StakeType.SINGLE;
        stake.singleNumberBet = new SingleBet();
        stake.singleNumberBet.numberChosen = 5;
        stake.singleNumberBet.amount = new BigDecimal(30);

        gameRequest.stakes = Arrays.asList(stake);

        ResponseEntity<GameResponse> response = template.postForEntity("/roulette/api/v1/play?forcedSpinNumber=5", gameRequest, GameResponse.class);
        assertNotNull(response);
        assertEquals(1080, response.getBody().getAmountWon().longValue());
    }

    @Test
    public void test_roulettePlayRedBlackBet() throws Exception {

        GameRequest gameRequest = new GameRequest();

        Stake stake = new Stake();
        stake.stakeType = StakeType.HALFNUMBER;
        stake.halfNumberBet = new HalfNumberBet();
        stake.halfNumberBet.firstHalf = true;
        stake.halfNumberBet.amount = new BigDecimal(30);

        gameRequest.stakes = Arrays.asList(stake);

        ResponseEntity<GameResponse> response = template.postForEntity("/roulette/api/v1/play?forcedSpinNumber=5", gameRequest, GameResponse.class);
        assertNotNull(response);
        assertEquals(60, response.getBody().getAmountWon().longValue());
    }

    @Test
    public void test_roulettePlaySquareBet() throws Exception {

        GameRequest gameRequest = new GameRequest();

        Stake stake = new Stake();
        stake.stakeType = StakeType.SQUARE;
        stake.squareBet = new SquareBet();
        stake.squareBet.topLeftNumber = 1;
        stake.squareBet.amount = new BigDecimal(30);

        gameRequest.stakes = Arrays.asList(stake);

        ResponseEntity<GameResponse> response = template.postForEntity("/roulette/api/v1/play?forcedSpinNumber=5", gameRequest, GameResponse.class);
        assertNotNull(response);
        assertEquals(270, response.getBody().getAmountWon().longValue());
    }
}
