    package com.example.git.transports;

    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;


    public abstract class Transport
    {
        private int id;
        private final long birthTime;
        private final long lifetime;
        final ImageView imgView;
        public Transport(int x, int y, Image img, int id, long birthTime, long lifetime){
            this.id = id;
            this.birthTime = birthTime;
            this.lifetime = lifetime;
            imgView = new ImageView(img);
            imgView.setLayoutX(x);
            imgView.setLayoutY(y);
            imgView.setFitWidth(80);
            imgView.setFitHeight(90);
        }
        public ImageView getImageView() {return imgView;}
    }
