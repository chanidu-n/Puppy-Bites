package com.example.puppybites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityEducationalContent extends AppCompatActivity {

    private ListView listView;
    private List<String> contentList;
    private Button btnViewItems;
    private ImageView imgPet;
    private TextView txtPetDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educational_content);

        listView = findViewById(R.id.lstEducationalContent);
        imgPet = findViewById(R.id.imgPet);
        txtPetDescription = findViewById(R.id.txtPetDescription);
        btnViewItems = findViewById(R.id.btnViewItems);

        contentList = new ArrayList<>();
        loadContent();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contentList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedContent = contentList.get(position);
                openContent(selectedContent);
            }
        });

        imgPet.setImageResource(R.drawable.main);
        txtPetDescription.setText("Learn how to take care of your furry friends with expert advice on nutrition, grooming, and overall pet health.");


        btnViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityEducationalContent.this, ActivityView.class);
                startActivity(intent);
            }
        });
    }

    private void loadContent() {
        contentList.add("Dog Nutrition Basics");
        contentList.add("Best Foods for Puppies");
        contentList.add("Essential Vitamins for Dogs");
        contentList.add("Feeding Adult Dogs: A Guide");
        contentList.add("Dog Breeds and Dietary Needs");
        contentList.add("Health Tips for Senior Dogs");
        contentList.add("Video: Dog Nutrition 101");
    }

    private void openContent(String contentTitle) {
        String url = "";
        switch (contentTitle) {
            case "Dog Nutrition Basics":
                url = "https://www.petmd.com/dog/nutrition/evr_dg_whats_in_a_balanced_dog_food";
                break;
            case "Best Foods for Puppies":
                url = "https://www.dogfoodadvisor.com/best-dog-foods/";
                break;
            case "Essential Vitamins for Dogs":
                url = "https://www.usnews.com/360-reviews/pets/best-dry-dog-food";
                break;
            case "Feeding Adult Dogs: A Guide":
                url = "https://www.webmd.com/pets/dogs/feeding-time";
                break;
            case "Dog Breeds and Dietary Needs":
                url = "https://carsonandbearpets.com/why-different-dog-breeds-need-different-diets/";
                break;
            case "Health Tips for Senior Dogs":
                url = "https://www.quora.com/What-kind-of-food-and-nutrition-do-different-breeds-of-dogs-need";
                break;
            case "Video: Dog Nutrition 101":
                url = "https://www.youtube.com/watch?v=waGpipMMe3A";
                break;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
