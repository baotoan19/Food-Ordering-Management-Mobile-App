package com.example.quanlycuahangbandoanvat.BUS;

import com.example.quanlycuahangbandoanvat.DTO.Food;

import java.util.ArrayList;
import java.util.Collections;

public class FoodBUS {
    private ArrayList<Food> listFood = new ArrayList<>();

    public ArrayList<Food> getListFood() {
        return listFood;
    }

    public void setListFood(ArrayList<Food> listFood) {
        this.listFood = listFood;
    }

    public FoodBUS(ArrayList<Food> listFood) {
        this.listFood = listFood;
    }

    public FoodBUS() {

    }
    public ArrayList<Food> getAll() {
        return this.listFood;
    }

    public Food getByIndex(int index) {
        return this.listFood.get(index);
    }

    public Food getByFoodID(String id) {
        int vitri = -1;
        int i = 0;
        // Sử dụng vòng lặp for để duyệt qua danh sách và kiểm tra kích thước danh sách trong mỗi lần lặp
        for (i = 0; i < this.listFood.size(); i++) {
            if (this.listFood.get(i).getFood_ID().equals(id)) {
                vitri = i;
                break; // Thoát khỏi vòng lặp ngay khi tìm thấy
            }
        }
        // Kiểm tra xem vitri có được cập nhật hay không, nếu không thì trả về null
        if (vitri != -1) {
            return this.listFood.get(vitri);
        } else {
            return null; // Trả về null nếu không tìm thấy id
        }
    }


    public int getIndexByFoodID(String id) {
        int i = 0;
        int vitri = -1;
        while (i < this.listFood.size() && vitri == -1) {
            if (listFood.get(i).getFood_ID().equals(id)) {
                vitri = i;
            } else {
                i++;
            }
        }
        return vitri;
    }

//    public ArrayList<Food> search(String text) {
//        text = text.toLowerCase();
//        ArrayList<Food> result = new ArrayList<>();
//        for (Food i : this.listFood) {
//            if (i.getFood_Name().toLowerCase().contains(text)) {
//                result.add(i);
//            }
//        }
//        return result;
//    }

    public ArrayList<Food> search(String text) {
        text = text.toLowerCase();
        ArrayList<Food> result = new ArrayList<>();
        for (Food i : this.listFood) {
            if (i.getFood_Name().toLowerCase().contains(text)) {
                result.add(i);
            }
        }
        return result;
    }
    public ArrayList<Food> getRandomFoods(int numRandomFoods) {
        if (numRandomFoods <= 0 || numRandomFoods > this.listFood.size()) {
            throw new IllegalArgumentException("Number of random foods must be between 1 and the size of the list.");
        }

        ArrayList<Food> result = new ArrayList<>(this.listFood);
        Collections.shuffle(result);
        return new ArrayList<>(result.subList(0, numRandomFoods));
    }
    public ArrayList<Food>getFoodByCategory(String category_ID)
    {
        ArrayList<Food> newFood=new ArrayList<>();
        for (Food Item :this.listFood)
        {
            if(Item.isFood_isDeleted()==false)
            {
                if(Item.getFood_Category_ID().equals(category_ID))
                {
                    newFood.add(Item);
                }
            }
        }
        return  newFood;
    }
    public int getTotalFood() {
        return this.listFood.size();
    }
}
