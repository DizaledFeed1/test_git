    package com.example.git.transports;

    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;

    import java.io.Serializable;


    public abstract class Transport implements Serializable
    {
        private int id;
        private int x,y,finalX,finalY;
        private final long lifetime;
        transient ImageView imgView;
        public Transport(int x, int y,int finalX,int finalY, Image img, int id, long lifetime){
            this.id = id;
            this.finalX = finalX;
            this.finalY = finalY;
            this.lifetime = lifetime;
            this.x = x;
            this.y = y;
            imgView = new ImageView(img);
            imgView.setLayoutX(x);
            imgView.setLayoutY(y);
            imgView.setFitWidth(200);
            imgView.setFitHeight(150);
        }
        public void setImgView(ImageView imgView){this.imgView = imgView;}
        public long getLifetime(){return lifetime;}
        public int getX(){return x;}
        public int getY(){return y;}
        public ImageView getImageView() {return imgView;}
        public int getId(){return id;}
        public  int getFinalX(){return finalX;}
        public  int getFinalY(){return finalY;}
    }
