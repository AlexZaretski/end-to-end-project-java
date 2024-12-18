package org.multicookers;

public class UpgradeDecorator extends MulticookerDecorator {
   private double capacityRate, wattageRate;
   int pr;
   int pr2;
   int pr3;
   int pr4;
   int pr5;
   int pr6;
   String present;

   public UpgradeDecorator(Multicooker multicooker, int capacityRate, int wattageRate, String present) {
      super(multicooker);
      this.pr = this.multicooker.getCapacity();
      this.pr2 = this.multicooker.getWattage();
      this.capacityRate = capacityRate;
      this.wattageRate = wattageRate;
      this.pr3 = capacityRate;
      this.pr4 = wattageRate;
      this.pr5 = this.pr + this.pr3;
      this.pr6 = this.pr2 + this.pr4;
      this.present = present;
   }

   public int getCapacity() {
      return pr5;
   }

   public int getWattage() {
      return pr6;
   }

   public String getPresent(){
      return present;
   }

   public String toString() {
      return this.multicooker.toString() + " with upgraded parameters: " + this.getCapacity() + " " + this.getWattage()+ " and present:" + this.getPresent();
   }
}
