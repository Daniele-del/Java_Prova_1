package trail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class Trail {

	private int bibNumber = 1;
	private int locationOrder = 0;

	public Map<Integer, Runner> runners = new TreeMap<>(); // numero runner , oggetto runner
	public Map<String, Location> locations = new TreeMap<>(); // nome location , oggetto location
	public Map<String, String> delegates = new TreeMap<>(); // id , delegato

	public Map<String, List<String>> locationDelegates = new HashMap<>(); // nome location , id delegati
	public Map<String, List<String>> delegateLocations = new TreeMap<>(); // id delegato , locations

	public Map<String, Map<Integer, Long>> passageMap = new TreeMap<>(); // nome location , numero runner , tempo
																			// passaggio
	public Map<Integer, Long> map = new HashMap<>(); // numero runner , tempo passaggio

	public int newRunner(String name, String surname) {
		Runner runner = new Runner(name, surname, bibNumber);
		runners.put(bibNumber, runner);
		bibNumber++;
		return runner.getBibNumber();
	}

	public Runner getRunner(int bibNumber) {
		return runners.get(bibNumber);
	}

	public Collection<Runner> getRunner(String surname) {
		List<Runner> listaPerCognome = new ArrayList<>();

		for (Runner runner : runners.values()) {
			if (runner.getSurname().equals(surname)) {
				listaPerCognome.add(runner);
			}
		}

		return listaPerCognome;
	}

	public List<Runner> getRunners() {
		List<Runner> listaRunners = new ArrayList<>();
		listaRunners.addAll(runners.values());
		return listaRunners;
	}

	public List<Runner> getRunnersByName() {

		List<Runner> listaPerNome = runners.values().stream()
				.sorted((r1, r2) -> r2.getSurname().compareTo(r1.getSurname())).collect(Collectors.toList());
		listaPerNome.stream().sorted((r1, r2) -> r2.getName().compareTo(r1.getName())).collect(Collectors.toList());

		return listaPerNome;
	}

	public void addLocation(String location) {
		Location locationName = new Location(location, locationOrder);
		locations.put(location, locationName);
		locationOrder++;
	}

	public Location getLocation(String location) {
		return locations.get(location);
	}

	public List<Location> getPath() {
		List<Location> listaOrderLocation = new ArrayList<>();
		listaOrderLocation.addAll(locations.values());
		return listaOrderLocation;
	}

	public void newDelegate(String name, String surname, String id) {
		String delegate = surname + "," + name + "," + id;
		delegates.put(id, delegate);
	}

	public List<String> getDelegates() {
		List<String> listaOrderDelegate = delegates.values().stream().sorted().collect(Collectors.toList());

		return listaOrderDelegate;
	}

	public void assignDelegate(String location, String delegate) throws TrailException {
		List<String> delLocation = new ArrayList<>();
		List<String> locDelegate = new ArrayList<>();

		if (!locations.containsKey(location)) {
			throw new TrailException();
		}
		if (!delegates.containsKey(delegate)) {
			throw new TrailException();
		}

		if (locationDelegates.containsKey(location) && !locationDelegates.get(location).contains(delegate)) {
			locationDelegates.get(location).add(delegate);
		}
		if (!locationDelegates.containsKey(location)) {
			delLocation.add(delegate);
			locationDelegates.put(location, delLocation);
		}

		if (delegateLocations.containsKey(delegate) && !delegateLocations.get(delegate).contains(location)) {
			delegateLocations.get(delegate).add(location);
		}
		if (!delegateLocations.containsKey(delegate)) {
			locDelegate.add(location);
			delegateLocations.put(delegate, locDelegate);
		}

	}

	public List<String> getDelegates(String location) {
		List<String> ordinata = new ArrayList<>();
		for (String delegato : locationDelegates.get(location)) {
			if (delegates.containsKey(delegato)) {
				ordinata.add(delegates.get(delegato));
			}
		}
		Collections.sort(ordinata);
		System.out.println(ordinata);
		return ordinata;
	}

	public long recordPassage(String delegate, String location, int bibNumber) throws TrailException {

		if (!delegates.containsKey(delegate)) {
			throw new TrailException();
		}
		if (!delegateLocations.get(delegate).contains(location)) {
			throw new TrailException();
		}
		if (!locations.containsKey(location)) {
			throw new TrailException();
		}
		if (!runners.containsKey(bibNumber)) {
			throw new TrailException();
		}

		long time = System.currentTimeMillis();

		map.put(bibNumber, time);
		Map<Integer, Long> sorted = map.entrySet().stream().sorted(comparingByValue())
				.collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
		passageMap.put(location, sorted);

		runners.get(bibNumber).checkPoints.add(time);

		return time;
	}

	public long getPassTime(String position, int bibNumber) throws TrailException {

		if (!locations.containsKey(position)) {
			throw new TrailException();
		}
		if (!runners.containsKey(bibNumber)) {
			throw new TrailException();
		}

		if (passageMap.containsKey(position) && passageMap.get(position).containsKey(bibNumber)) {
			return passageMap.get(position).get(bibNumber);
		}

		return -1;
	}

	public List<Runner> getRanking(String location) {
		List<Runner> listaRunnerForTime = new ArrayList<>();

		for (int runner : passageMap.get(location).keySet()) {
			listaRunnerForTime.add(runners.get(runner));
		}

		return listaRunnerForTime;
	}

	public List<Runner> getRanking() {
		List<Runner> listaRunnerForTime = new ArrayList<>();
		listaRunnerForTime.addAll(runners.values());

		Collections.sort(listaRunnerForTime, new Comparator<Runner>() {
			@Override
			public int compare(Runner o1, Runner o2) {
				int i = Integer.compare(o1.checkPoints.size(), o2.checkPoints.size());

				if (i != 0)
					return -i;

				return Long.compare(o1.checkPoints.get(o1.checkPoints.size() - 1),
						o2.checkPoints.get(o2.checkPoints.size() - 1));
			}

		});

		return listaRunnerForTime;
	}
}
