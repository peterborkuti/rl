import { TestBed } from '@angular/core/testing';

import { RandomGeneratorService } from './random-generator.service';

describe('RandomGeneratorService', () => {
  let service: RandomGeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RandomGeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should give a number between 0 and 1', () => {
    const n = service.getRandomNumber();
    expect(n).toBeGreaterThanOrEqual(0);
    expect(n).toBeLessThan(1);
  })
});
