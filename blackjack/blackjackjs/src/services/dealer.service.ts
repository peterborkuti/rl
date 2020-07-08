import { Injectable } from '@angular/core';
import { RandomGeneratorService } from './random-generator.service';

@Injectable({
  providedIn: 'root'
})
export class DealerService {

  constructor(private randomGenerator: RandomGeneratorService) {}

  public getCard(): number {
    return this.getRandom(2,11);
  }

  /**
   * getRandom(max) - random number between 0 and max
   * getRandom() - random number between 0 and 1
   * getRandom(min, max) - random number between min and max
   * @param limits
   */

  public getRandom(...limits: number[]): number {
    if (!limits || limits.length === 0) {
      return this.randomGenerator.getRandomNumber();
    }

    if (limits.length === 1) {
      return this.randomGenerator.getRandomNumber() * limits[0];
    }

    return this.randomGenerator.getRandomNumber() * (limits[1] - limits[0]) + limits[0];
  }

}
