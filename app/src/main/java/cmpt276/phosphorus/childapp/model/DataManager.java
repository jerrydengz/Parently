package cmpt276.phosphorus.childapp.model;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {

    private static DataManager instance;
    private final Map<DataType, File> files;

    public DataManager(Context context) {
        this.files = new HashMap<>();
        File dir = context.getFilesDir();
        for (DataType type : DataType.values()) {
            this.files.put(type, new File(dir, type.getFileName()));
        }
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    public void saveData(DataType dataType) {
        try {
            File targetFile = this.files.get(dataType);
            Writer writer = new FileWriter(targetFile);
            this.getGson().toJson(dataType.getDataToSave(), writer);
            writer.close();
        } catch (IOException ignored) {
        }
    }

    public <T> void loadData(DataType dataType) {
        List<T> result;
        try {
            File targetFile = this.files.get(dataType);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(targetFile));
            // Ref https://stackoverflow.com/questions/20773850/gson-typetoken-with-dynamic-arraylist-item-type
            Type type = TypeToken.getParameterized(ArrayList.class, dataType.getTypeClass()).getType();
            List<T> newResult = this.getGson().fromJson(bufferedReader, type);
            result = (newResult == null) ? new ArrayList<>() : newResult;
            bufferedReader.close();
        } catch (IOException e) {
            result = new ArrayList<>();
        }
        dataType.load(result);
    }

    //got from https://stackoverflow.com/questions/39192945/serialize-java-8-localdate-as-yyyy-mm-dd-with-gson
    //gets a Gson object capable of interacting with a LocalDateTime object
    private Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
    }
}
