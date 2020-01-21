package serveur;

class loader extends Thread {
    boolean showProgress = true;
    public void run() {
        String anim= "⣾⣽⣻⢿⡿⣟⣯⣷";
        int x = 0;
        while (showProgress) {
            System.out.print("\r["+anim.charAt(x++ % anim.length())+"] Generating Key ");
            try { Thread.sleep(400); }
            catch (Exception e) {};
        }
        System.out.print("\r[Ok] Generating Key ");
        System.out.println("\nKey Generated");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}