package com.cat.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GWTest {

	private static final String dir = "F:\\Desktop\\log";
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}).*(\\{.*}).*");

	public static void main(String[] args) throws IOException {
		int year = 2016, month = 10;

		for (int day = 1; day < 30; day++) {
			for (int hour = 0; hour < 23; hour++) {
				long date = getDate(year, month, day, hour);
//				System.out.println(date);
				System.out.println(date + " " + getDevice(date));
			}
		}

		//System.out.println(total("F:\\Desktop\\DataReceive.txt.2016092818.log"));

	}

	private static int total(String name) throws IOException {
		Set<String> set = new HashSet<>();
		Path path = Paths.get(name);

		if (!Files.exists(path)) {
			System.err.println(name + "不存在");
			return -1;
		}

		BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				set.add(parse(matcher.group(2)));
			}
		}
		return set.size();
	}

	private static long getDate(int year, int month, int day, int hour) {
		return (long) (year * 10e5 + month * 10e3 + day * 10e1 + hour);
	}

	//time:2016102319
	private static int getDevice(long date) throws IOException {
		Set<String> set = new HashSet<>();

		String name = "DataReceive.txt." + date + ".log";
		Path path = Paths.get(dir, name);

		if (!Files.exists(path)) {
			//System.err.println(name + "不存在");
			return -1;
		}

		BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String time = matcher.group(1);
				String action = matcher.group(2);
				String dev = parse(action);
				set.add(dev);
				//System.out.printf("%s\t%s%n", "", dev);
				//System.out.println(time + " " + action + " " + dev);
			}
		}
		return set.size();
	}

	private static String parse(String action) throws IOException {
		Map map = mapper.readValue(action, Map.class);
		return (String) map.get("devSN");
	}

	private static LocalDateTime convert(String str) {
		if (str == null || str.isEmpty()) {
			return null;
		}
		LocalDateTime date = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			date = LocalDateTime.parse(str, formatter);
		} finally {
		}
		return date;
	}

	private static boolean filter(LocalDateTime current, LocalDateTime start, LocalDateTime end) {
		boolean pass = true;
		if (start != null) {
			pass = current.isAfter(start) || current.isEqual(start);
		}
		if (end != null) {
			pass = current.isBefore(end) || current.isEqual(end);
		}
		return pass;
	}

	private static boolean filter(String current, String start, String end) {
		return filter(convert(current), convert(start), convert(end));
	}
}
