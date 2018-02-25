/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: marinap
 *
 * Created on February 13, 2018, 10:04 AM
 */

#include <cstdlib>
#include <stdexcept>    // exceptions
//#include <exception>

#include "FormatUtils.hpp"
#include "Parameter.hpp"

/*
 * 
 */

// Global variables
const std::string help("-h");
const std::string confFlag("-conf");
const std::string param("-param");
const std::string data("-data");
const std::string prop("-prop");
const std::string deriv("-deriv");
const std::string lj("-LJ");
const std::string out("-out");

int main(int argc, char** argv) {
    /*
     * Global variables
     */
    
    // Local variables
    Config conf = Config();
    // Vector of atomTypes
    std::vector<AtomType> atomTypeVec;
    // Vector of unique prms
    std::vector<Parameter> prmVec;
    // Vector of constrained prms
    std::vector<Parameter*> constrPrmVec;
    
    
    
    // Flag to indicate that compound and parameter vectors
    // were properly created
    bool isPrmVecCreated = false;
    bool isCmpVecCreated = false;
    bool isDerivCreated  = false;
    bool isLJconstrained = false;
    
    try {
        if (argc < 2) {
            //printUsage();
            throw std::invalid_argument("\t(main)\n"
                    "\tNo argument was specified.\n");
        }
        
        int i = 1;
        std::string arg;
        while (i < argc) {
            
            arg = argv[i];
            
            if (argv[i][0] == '-') {
                // for help
                if (arg.compare(help) == 0) {
                    StringUtils::printUsage();
                    return 0;
                    
                } else if (arg.compare(confFlag) == 0) {
                    i++;
                    FormatUtils::readConf(argv[i], conf);
                    i++;
                    
                } else if (arg.compare(lj) == 0) {
                    i++;
                    FormatUtils::readMatrix(argv[i], atomTypeVec);
                    //AtomType::print(atomTypeVec);
                    isLJconstrained  = true;
                    i++;
                } else if (arg.compare(param) == 0) {
                    i++;
                    FormatUtils::readParam(argv[i], prmVec, constrPrmVec, atomTypeVec);
                    isPrmVecCreated = true;
                    i++;
                } else {
                    return 0;
                }
            }
        }
        
        
        
    }  catch (const std::invalid_argument &e) {
        std::cerr << "\nInvalid argument:\n " << e.what() << '\n';
    }  catch (const std::logic_error &e) {
        std::cerr << e.what() << '\n';
    }  catch (const std::ios_base::failure &e) {
        std::cerr << e.what() << '\n';
    }  catch (const ConversionExcept &e) {
        std::cerr << e.what() << '\n';
    }  catch (const IOExcept &e) {
        std::cerr << e.what() << '\n';
    }
    
    return 0;
}

