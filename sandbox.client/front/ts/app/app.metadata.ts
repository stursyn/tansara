//
// consts


export const maxFileSize: number = 10887500; //10MB
export const maxScore: number = 4;
export const dateFormat: string = "dd.MM.yyyy HH:mm";
export const dmyDateFormat: string = "dd.MM.yyyy";
export const langKey: string = "gg_lang";
export const inputMaxLength: number = 300;

export const GG_LOCALE = "ru-KZ";
export const GG_FORMATS = {
  parse: {
    dateInput: 'L',
  },
  display: {
    dateInput: 'L',
    monthYearLabel: 'M YYYY',
    dateA11yLabel: 'll',
    monthYearA11yLabel: 'M YYYY',
  },
};

export const pageSizeOptions: Array<number> = [5, 10, 15, 25, 50, 100, 150, 200];
export const DEFAULT_PAGE_SIZE = 10;
export const MAX_SCORE = 4.0;

//
// classes

export class RouteInfo {
  cat?: string;
  path: string;
  title: string;
  icon?: string;
  class?: string;
  mayHaveNew?: boolean;
  needToRoute?: boolean;
  canIFunc?: string[];
}

export class OptionMenuItem {
  path: string;
  title: string;
}

export class AuthChangedInfo {
  isAuthenticated: boolean = false;
  displayName: string;
  roleItems: Role[] = [];
  rootPath: string = "/";
  currentModeCode: string;
  menuItems: OptionMenuItem[] = [];
}

export class Role {
  code: string;
  name: string;

  constructor(code?: string, name?: string) {
    this.code = code;
    this.name = name;
  }
}

//
// Functions


export function checkOnlyInteger(event) {
  if (event.key == '.' || event.key == 'e' || event.key == '-') {
    event.preventDefault();
  }
}

export function checkOnlyPositiveNumber(event) {
  if (event.key == 'e' || event.key == '-') {
    event.preventDefault();
  }
}

export function toCamelCaseLang(str: string): string {
  if (str == null) return null;
  switch (str.toUpperCase()) {
    case 'KK':
      return 'Kk';
    case 'RU':
      return 'Ru';
    case 'EN':
      return 'En';
    default:
      return null;
  }
}
