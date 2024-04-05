    package com.example.git.transports;

    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;


    public abstract class Transport
    {
        private int id;
        private final long lifetime;
        final ImageView imgView;
        public Transport(int x, int y, Image img, int id, long lifetime){
            this.id = id;
            this.lifetime = lifetime;
            imgView = new ImageView(img);
            imgView.setLayoutX(x);
            imgView.setLayoutY(y);
            imgView.setFitWidth(220);
            imgView.setFitHeight(200);
        }
        public ImageView getImageView() {return imgView;}
        public int getId(){return id;}
    }
