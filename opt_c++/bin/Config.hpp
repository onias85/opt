/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   Config.hpp
 * Author: marinap
 *
 * Created on February 13, 2018, 10:09 AM
 */

#ifndef CONFIG_HPP
#define CONFIG_HPP

#include"StringUtils.hpp"

class Config {
public:
    Config();
    Config(const Config& orig);
    
    static const int COLNAME = 0;
    static const int COLVAL  = 1;
    
    void setnStep(int n) { nStep = n; }
    void setTol(double v) { tol = v; }
    void setStepS(double v) { stepS = v; }
    void setStepL(double v) { stepL = v; }
    void setGradAllowed(double v) { gradAllowed = v; }
    void setMaxSize(double v) { maxSize = v; }
    
    int getnStep() { return nStep; }
    double getTol() { return tol; }
    double getStepS() { return stepS; }
    double getStepL() { return stepL; }
    double getGradAllowed() { return gradAllowed; }
    double getMaxSize() { return maxSize; }
    
    void print();
    
private:
    int nStep;              // number of steps
    double tol;             // tolerance
    double stepS;           // small step
    double stepL;           // large step
    double gradAllowed;     // % gradient allowed
    double maxSize;         // max value prm can change
};

#endif /* CONFIG_HPP */

