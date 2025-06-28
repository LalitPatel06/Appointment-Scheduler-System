import java.util.LinkedHashMap;

public class DoctorHelper {

    private static final LinkedHashMap<String, String[]> doctorMap = new LinkedHashMap<>();

    static {
        doctorMap.put("Chest", new String[]{"Dr. Sharma", "Dr. Mehta"});
        doctorMap.put("Heart", new String[]{"Dr. Kapoor", "Dr. Rani"});
        doctorMap.put("Skin", new String[]{"Dr. Batra", "Dr. Jain"});
        doctorMap.put("Teeth", new String[]{"Dr. Mitali", "Dr. Joshi"});
        doctorMap.put("Hair", new String[]{"Dr. Gupta", "Dr. Das"});
    }

    public static String[] getAllProblems() {
        return doctorMap.keySet().toArray(new String[0]);
    }

    public static String[] getDoctorsForProblem(String problem) {
        return doctorMap.getOrDefault(problem, new String[0]);
    }
}
