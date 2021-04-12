package pl.adamsiedlecki.OTM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import pl.adamsiedlecki.OTM.db.tempData.TemperatureDataService;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAlias;
import pl.adamsiedlecki.OTM.db.tempDataAlias.TempDataAliasService;
import pl.adamsiedlecki.OTM.tools.ChartCreator;

@SpringBootApplication
public class OtmApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(OtmApplication.class, args);
		Environment env = run.getEnvironment();

		TempDataAliasService tempDataAliasService = run.getBean(TempDataAliasService.class);
		if(tempDataAliasService.count() == 0){
			TempDataAlias alias1 = new TempDataAlias();
			alias1.setOriginalName("t1");
			alias1.setAliasName("staw");
			alias1.setLatitude(Float.parseFloat(env.getProperty("t1.latitude", "")));
			alias1.setLongitude(Float.parseFloat(env.getProperty("t1.longitude", "")));

			TempDataAlias alias2 = new TempDataAlias();
			alias2.setOriginalName("t2");
			alias2.setAliasName("dom");
			alias2.setLatitude(Float.parseFloat(env.getProperty("t2.latitude", "")));
			alias2.setLongitude(Float.parseFloat(env.getProperty("t2.longitude", "")));

			tempDataAliasService.save(alias1);
			tempDataAliasService.save(alias2);
		}

//		DataFetcher dataFetcher = run.getBean(DataFetcher.class);
//		List<TemperatureData> list = dataFetcher.fetch();
//		for(TemperatureData data : list){
//			System.out.println(data);
//		}
////
		TemperatureDataService temperatureDataService = run.getBean(TemperatureDataService.class);
//		Optional<List<TemperatureData>> lastTemperatures = temperatureDataService.getLastTemperatures();
//		if(lastTemperatures.isPresent()){
//			System.out.println(lastTemperatures);
//
//		}

		ChartCreator chartCreator = new ChartCreator();
		chartCreator.createOvernightChart(temperatureDataService.findAllLastXHours(12).get());

	}

}
