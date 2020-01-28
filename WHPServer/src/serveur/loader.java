package serveur;

class loader extends Thread {
    boolean showProgress = true;
    public void run() {
        String anim= "⣾⣽⣻⢿⡿⣟⣯⣷";
        int x = 0;
        System.out.println("\u001B[32mStarting KeyGen");
        while (showProgress) {
            System.out.print("\r["+anim.charAt(x++ % anim.length())+"] Generating Key ");
            try { Thread.sleep(400); }
            catch (Exception e) {
                System.err.println(e);
            };
        }
        System.out.print("\r\u001B[32m[Ok] Generating Key ");
        System.out.println("\n\u001B[32mKey Generated");
        System.out.println("\u001B[32mServer started\u001B[0m");
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