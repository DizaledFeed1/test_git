    package com.example.git.transports;

    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;


    public abstract class Transport
    {
        private int id;
        private int finalX,finalY;
        private final long lifetime;
        final ImageView imgView;
        public Transport(int x, int y,int finalX,int finalY, Image img, int id, long lifetime){
            this.id = id;
            this.finalX = finalX;
            this.finalY = finalY;
            this.lifetime = lifetime;
            imgView = new ImageView(img);
            imgView.setLayoutX(x);
            imgView.setLayoutY(y);
            imgView.setFitWidth(200);
            imgView.setFitHeight(150);
        }
        public ImageView getImageView() {return imgView;}
        public int getId(){return id;}
        public  int getFinalX(){return finalX;}
        public  int getFinalY(){return finalY;}
    }
