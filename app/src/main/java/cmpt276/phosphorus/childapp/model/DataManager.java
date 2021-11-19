package cmpt276.phosphorus.childapp.model;

import android.content.Context;
import android.provider.ContactsContract;

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
import java.util.List;

public class DataManager {
    private File file;

    public DataManager(String fileName, Context context){
        File dir = context.getFilesDir();
        this.file = new File(dir, fileName);
    }

    public <T> void saveData(List<T> lst){
        Gson gson = getGson();
        try {
            Writer writer = new FileWriter(file);//writes to designated file
            gson.toJson(lst, writer);//writes this.allChildren to the file
            writer.close();
        } catch (IOException ignored) {
        }
    }

    public <T> List<T> loadData(){
        Gson gson = getGson();
        List<T> result;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));//reads from designated file
            Type childType = new TypeToken<List<Child>>() {
            }.getType();//gson uses this to parse the Child type
            result = gson.fromJson(bufferedReader, childType);//loads contents as allChildren
            if (result == null) {
                result = new ArrayList<>();
            }//if file is empty
            bufferedReader.close();
        } catch (IOException e) {
            result = new ArrayList<>();
        }
        return result;
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
