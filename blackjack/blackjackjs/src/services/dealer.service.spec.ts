import { TestBed } from '@angular/core/testing';

import { DealerService } from './dealer.service';
import { RandomGeneratorService } from './random-generator.service';

const randomService: RandomGeneratorService = {
  getRandomNumber: () => 0.5
}

describe('DealerService', () => {
  let service: DealerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [{provide: RandomGeneratorService, useValue: randomService}]
    });

    service = TestBed.inject(DealerService, );
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getRandomNumber should give 0.5', () => {
    expect(service.getRandom()).toBe(0.5);
  })

  it('getRandomNumber(4) should give 2', () => {
    expect(service.getRandom(4)).toBe(2);
  })

  it('getRandomNumber(2,4) should give 3', () => {
    expect(service.getRandom(2,4)).toBe(3);
  })

  it('getCard should give half between 2 and 11', () => {
    expect(service.getCard()).toBe(6.5);
  })
});
