package OpenHouse.AI.CodingExerciseNGG;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <h1>CodingExerciseNggApplication</h1>
 * This program is created as port of the application process for a Backend Software Developer position.
 * Main objective is to receive log entries and store them into memory while having the capability to retrieve them.
 * Functionalities include consuming POST requests and parsing them into memory and
 * GET requests to scan memory and return results
 *
 * @author  Neil Gilbert Gallardo
 * @version 1.0
 * @since   2022-06-10
 */
@SpringBootApplication
@RestController
public class CodingExerciseNggApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingExerciseNggApplication.class, args);
	}

	/**
	 * This method is used to search the memory for logs and return anything that fit the criteria.
	 * @param userId A user's ID in which will be checked if any logs matches
	 * @param type  The type of interaction taken by the user and will be checked if any logs matches
	 * @param fromDatetime  The starting date that will checked if any logs are logged after this date
	 * @param toDatetime  The ending date that will checked if any logs are logged before this date
	 * @return String the response of the get action.
	 */
	@RequestMapping(value = "/logs/get",
			method = RequestMethod.GET,
			produces = "application/json")
	public String getLogs(@RequestParam(value = "userId", required = false) String userId,
						  @RequestParam(value = "type", required = false) String type,
						  @RequestParam(value = "from", required = false)
							  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
						  	  LocalDateTime  fromDatetime,
						  @RequestParam(value = "to", required = false)
							  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
							  LocalDateTime  toDatetime) throws Exception {
		//Search through logs
		ArrayList<JSONObject> logs = FileHandler.getLogFiles();
		ArrayList<JSONObject> filteredLogs = new ArrayList<>();
		for (JSONObject log: logs) {
			boolean toAdd = true;

			// Test if user ID isn't equal to parameter
			if(userId!=null && (!log.getString("userId").toLowerCase().equals(userId.toLowerCase()))){
				toAdd = false;
			}

			// Test if time is in range
			if(fromDatetime!=null || toDatetime!=null){
				if(fromDatetime!=null && toDatetime!=null){
					JSONArray logActions = log.getJSONArray("actions");
					for (int i = 0; i < logActions.length(); i++) {
						LocalDateTime actionDateTime = LocalDateTime.parse(logActions.getJSONObject(i).getString("time"),
								DateTimeFormatter.ISO_DATE_TIME);
						if(actionDateTime.isAfter(fromDatetime) && actionDateTime.isBefore(toDatetime)){
							toAdd = true;
							break;
						} else {
							toAdd = false;
						}
					}
				} else if (fromDatetime!=null){
					JSONArray logActions = log.getJSONArray("actions");
					for (int i = 0; i < logActions.length(); i++) {
						LocalDateTime actionDateTime = LocalDateTime.parse(logActions.getJSONObject(i).getString("time"),
								DateTimeFormatter.ISO_DATE_TIME);
						if(actionDateTime.isAfter(fromDatetime)){
							toAdd = true;
							break;
						} else {
							toAdd = false;
						}
					}
				} else if (toDatetime!=null){
					JSONArray logActions = log.getJSONArray("actions");
					for (int i = 0; i < logActions.length(); i++) {
						LocalDateTime actionDateTime = LocalDateTime.parse(logActions.getJSONObject(i).getString("time"),
								DateTimeFormatter.ISO_DATE_TIME);
						if(actionDateTime.isBefore(toDatetime)){
							toAdd = true;
							break;
						} else {
							toAdd = false;
						}
					}
				}

			}

			// Test if type isn't equal to parameter
			if(type!=null){
				JSONArray logActions = log.getJSONArray("actions");
				for (int i = 0; i < logActions.length(); i++) {
					if(logActions.getJSONObject(i).getString("type").toLowerCase()
							.equals(type.toLowerCase())){
						toAdd = true;
						break;
					} else {
						toAdd = false;
					}
				}
			}

			if(toAdd)
				filteredLogs.add(log);
		}

		JSONObject finalOutput = new JSONObject();
		for (int i = 0; i < filteredLogs.size(); i++) {
			finalOutput.put(i+"", filteredLogs.get(i));
		}
		return String.format(finalOutput.toString());
	}

	/**
	 * This method is used to add one log into the logging system.
	 * @param payload The JSON data for the log
	 * @return String the response of the post action.
	 */
	@RequestMapping(value = "/log/add",
			method = RequestMethod.POST,
			produces = "application/json")
	public String storeLog(@RequestBody String payload) throws Exception {
		JSONObject data = parseJSON(payload);
		FileHandler.createLog(data.getString("userId") + ":" + data.getString("sessionId"),
				data.toString());

		return String.format(buildSimpleResponse(true, "Log successfully stored"));
	}

	/**
	 * This method is used to add multiple logs into the logging system.
	 * @param payload The JSON data for the log
	 * @return String the response of the post action.
	 */
	@RequestMapping(value = "/logs/add",
			method = RequestMethod.POST,
			produces = "application/json")
	public String storeLogs(@RequestBody String payload) throws Exception {
		JSONObject data = parseJSON(payload);
		Iterator<String> keys = data.keys();

		while(keys.hasNext()){
			String key = keys.next();
			if(data.get(key) instanceof JSONObject){
				JSONObject iData = (JSONObject) data.get(key);
				FileHandler.createLog(iData.getString("userId") + ":" + iData.getString("sessionId"),
						iData.toString());
			}
		}

		return String.format(buildSimpleResponse(true, "Bulk of Logs successfully stored"));
	}

	/**
	 * This is a helper method, used to quickly parse a String into a JSONObject.
	 * @param data The String data to be parsed
	 * @return JSONObject the resulting JSONObject from the passed String data.
	 */
	private JSONObject parseJSON(String data){
		JSONObject json = new JSONObject(data);
		return json;
	}

	/**
	 * This is a helper method, used to construct a quick response message to be sent back to the user.
	 * @param success Will show to the user if the action succeeded
	 * @param message A custom message so the system can display any message to the user
	 * @return String the constructed JSON response to be passed back to the user.
	 */
	private String buildSimpleResponse(boolean success, String message){
		JSONObject json = new JSONObject();
		json.put("success", success);
		json.put("message", message);
		return json.toString();
	}
}
