import style from "./Button.module.scss";
import React from "react";

interface ButtonProps extends React.HTMLAttributes<HTMLButtonElement> {
  disabled?: boolean;
  children: React.ReactNode;
}

const Button = ({ disabled = false, ...props }: ButtonProps) => {
  return (
    <button
      onClick={disabled ? () => {} : props?.onClick}
      className={`${style.button} ${
        disabled ? style["disabled"] : null
      }`}
    >
      <span className={"button-text"}>{props.children}</span>
    </button>
  );
};

export default Button;
