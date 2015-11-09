package com.example.reggi.Flickr;

import java.util.HashMap;

public class Flickr {

    /*
    s small square 75x75
    t thumbnail, 100 on longest side
    q large square 150x150
    m small, 240 on longest side
    n small, 320 on longest side
    - medium, 500 on longest side
    z medium 640, 640 on longest side
    c medium 800, 800 on longest side†
    b large, 1024 on longest side*
    h large 1600, 1600 on longest side†
    k large 2048, 2048 on longest side†
     */
    private static int[] PHOTO_SIZES = new int[]{75, 100, 150, 240, 320, 500, 640, 800, 1024, 1600, 2048};
    private static String SIZE_CODES = "stqmn-zcbhk";
    private static HashMap<Integer, Integer> CODE_INDEX = new HashMap<>();

    public static String getPhotoUrl(Photo photo) {
        return "https://farm" + photo.mFarm + ".staticflickr.com/" + photo.mServer + "/" +
                photo.mId + "_" + photo.mSecret + ".jpg";
    }

    public static String getPhotoUrl(Photo photo, int size) {
        if (!CODE_INDEX.containsKey(size)) {
            int i = 0;
            for (; i < PHOTO_SIZES.length; i++) {
                if (size <= PHOTO_SIZES[i] || i == PHOTO_SIZES.length - 1) {
                    break;
                }
            }
            CODE_INDEX.put(size, i);
        }

        return "https://farm" + photo.mFarm + ".staticflickr.com/" + photo.mServer + "/" +
                photo.mId + "_" + photo.mSecret + "_" + SIZE_CODES.charAt(CODE_INDEX.get(size)) + ".jpg";
    }

    public static class Photos {
        public int mPage;
        public int mPages;
        public int mPerPage;
        public int mTotal;
    }

    public static class Photo {
        public long mId;
        public String mOwner;
        public String mSecret;
        public int mServer;
        public int mFarm;
        public String mTitle;
        public boolean mIsPublic;
        public boolean mIsFriend;
        public boolean mIsFamily;
    }
}
