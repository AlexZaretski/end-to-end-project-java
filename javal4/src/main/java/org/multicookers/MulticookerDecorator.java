// Source code is decompiled from a .class file using FernFlower decompiler.
package org.multicookers;

public abstract class MulticookerDecorator extends Multicooker {
   protected Multicooker multicooker;

   public MulticookerDecorator(Multicooker multicooker) {
      super(multicooker.getId(), multicooker.getType(), multicooker.getCapacity(), multicooker.getWattage(), multicooker.getPrice());
      this.multicooker = multicooker;
   }

   public String toString() {
      return this.multicooker.toString();
   }
}
