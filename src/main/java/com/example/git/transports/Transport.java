    package com.example.git.transports;

    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;


    public abstract class Transport
    {
        final ImageView imgView;
        public Transport(int x,int y, Image img){
            imgView = new ImageView(img);
            imgView.setLayoutX(x);
            imgView.setLayoutY(y);
            imgView.setFitWidth(80);
            imgView.setFitHeight(90);
        }
        public ImageView getImageView() {return imgView;}
    }
