package com.vinnymac.bittool;

import android.os.AsyncTask;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.service.marketdata.polling.PollingMarketDataService;
//import com.xeiam.xchange.bitcoincentral.BitcoinCentralExchange;
import com.xeiam.xchange.bitcoincharts.BitcoinChartsExchange;
import com.xeiam.xchange.bitstamp.BitstampExchange;
import com.xeiam.xchange.btce.BTCEExchange;
import com.xeiam.xchange.campbx.CampBXExchange;
import com.xeiam.xchange.mtgox.v1.MtGoxExchange;

public class UpdateMarket extends AsyncTask<String, Void, MarketSession> {

	private Exception exception;

	private MarketSession tickString;

	public UpdateMarket() {
		// Constructor may be parametric
	}

	@Override
	protected MarketSession doInBackground(String... params) {
		String exc = params[0];
		String cur = params[1];

		Ticker ticker;

		// com.xeiam.xchange.btce.BTCEExchange
		try {
			Class<?> cls = Class.forName(exc);
			Exchange exchange = ExchangeFactory.INSTANCE.createExchange(cls
					.getName());
			PollingMarketDataService markettickerService = exchange
					.getPollingMarketDataService();
			ticker = markettickerService.getTicker(Currencies.BTC, cur);

			tickString = new MarketSession(ticker.getTradableIdentifier().toString(),
					ticker.getLast().toString(), ticker.getBid().toString(),
					ticker.getAsk().toString(), ticker.getHigh().toString(),
					ticker.getLow().toString(), ticker.getVolume().toString(), "-1");
					//ticker.getTimestamp().toString());
					// TimeStamps seem to not work for all of the markets?

		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			tickString = new MarketSession("-1", "-1", "-1", "-1", "-1", "-1", "-1",
					"-1");
		} catch (Exception e) {
			e.printStackTrace();
			tickString = new MarketSession("-1", "-1", "-1", "-1", "-1", "-1", "-1",
					"-1");
		}

		return tickString;

	}
}
