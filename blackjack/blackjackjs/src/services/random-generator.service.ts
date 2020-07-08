import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RandomGeneratorService {

  constructor() { }

  public getRandomNumber(): number {
    return Math.random();
  }
}
