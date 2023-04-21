interface Rate {
  currency: string;
  code: string;
  mid: number;
}

export interface ExchangeTable {
  table: string;
  no: string;
  effectiveDate: string;
  rates: Rate[];
}