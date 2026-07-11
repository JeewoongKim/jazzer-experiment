import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonRoundTripFuzzer {
  public static void fuzzerTestOneInput(FuzzedDataProvider data) {
    String input = data.consumeRemainingAsString();

    try {
      Object parsed = new JSONTokener(input).nextValue();

      String serialized = JSONObject.valueToString(parsed);
      Object reparsed = new JSONTokener(serialized).nextValue();

      if (!sameJsonValue(parsed, reparsed)) {
        throw new AssertionError("JSON value changed after parse/serialize/reparse");
      }
    } catch (JSONException expected) {
      // Invalid JSON input is expected for arbitrary fuzzer input.
    }
  }

  private static boolean sameJsonValue(Object first, Object second) {
    JSONArray left = new JSONArray();
    left.put(first);

    JSONArray right = new JSONArray();
    right.put(second);

    return left.similar(right);
  }
}
